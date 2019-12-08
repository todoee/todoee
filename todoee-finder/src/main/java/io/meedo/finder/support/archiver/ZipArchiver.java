package io.meedo.finder.support.archiver;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;

import io.meedo.finder.core.Target;
import io.meedo.finder.core.Volume;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

/**
 * Zip Archiver Implementation.
 *
 * @author James.zhang
 */
public class ZipArchiver extends AbstractArchiver implements Archiver {

    private static final CRC32 crc32 = new CRC32();

    @Override
    public String getMimeType() {
        return "application/zip";
    }

    @Override
    public String getExtension() {
        return ".zip";
    }

    @Override
    public ArchiveEntry createArchiveEntry(String targetPath, long targetSize, byte[] targetBytes) {
        ZipArchiveEntry zipEntry = new ZipArchiveEntry(targetPath);
        zipEntry.setSize(targetSize);
        zipEntry.setMethod(ZipEntry.STORED);
        if (targetBytes != null) {
            zipEntry.setCrc(crc32Checksum(targetBytes));
        }
        return zipEntry;
    }

    @Override
    public ArchiveOutputStream createArchiveOutputStream(BufferedOutputStream bufferedOutputStream) {
        return new ZipArchiveOutputStream(bufferedOutputStream);
    }

    // for some internal optimizations should use the constructor that accepts a File argument
    public ArchiveOutputStream createArchiveOutputStream(Path path) throws IOException {
        return new ZipArchiveOutputStream(path.toFile());
    }

    @Override
    public ArchiveInputStream createArchiveInputStream(BufferedInputStream bufferedInputStream) throws IOException {
        return new ZipArchiveInputStream(bufferedInputStream);
    }

    @Override
    public Target compress(Target... targets) throws IOException {
        Target compressTarget = null;
        Path compressFile = null;

        ArchiveOutputStream archiveOutputStream = null;

        try {

            for (Target target : targets) {
                // get target volume
                final Volume targetVolume = target.getVolume();

                // gets the target infos
                final String targetName = targetVolume.getName(target);
                final String targetDir = targetVolume.getParent(target).toString();
                final boolean isTargetFolder = targetVolume.isFolder(target);

                if (compressFile == null) {
                    // create compress file
                    String compressFileName = (targets.length == 1) ? targetName : getArchiveName();
                    compressFile = Paths.get(targetDir, compressFileName + getExtension());

                    // creates a new compress file to not override if already exists
                    // if you do not want this behavior, just comment this line
                    compressFile = createFile(true, compressFile.getParent(), compressFile);

                    compressTarget = targetVolume.fromPath(compressFile.toString());

                    // open streams to write the compress target contents and auto close it
                    archiveOutputStream = createArchiveOutputStream(compressFile);
                }

                if (isTargetFolder) {
                    // compress target directory
                    compressDirectory(target, archiveOutputStream);
                } else {
                    // compress target file
                    compressFile(target, archiveOutputStream);
                }
            }

        } finally {
            // close streams
            if (archiveOutputStream != null) {
                archiveOutputStream.finish();
                archiveOutputStream.close();
            }
        }

        return compressTarget;
    }

    @Override
    public Target decompress(Target targetCompress) throws IOException {
        Target decompressTarget;

        final Volume volume = targetCompress.getVolume();

        // gets the compress target infos
        final String src = targetCompress.toString();
        final String dest = removeExtension(src);

        // create zipFile instance to read the compress target 
        // contents  and auto close it
        try (ZipFile zipFile = new ZipFile(src)) {

            // creates the decompress target infos
            Path decompressDir = Paths.get(dest);

            // creates a new decompress folder to not override if already exists
            // if you do not want this behavior, just comment this line
            decompressDir = createFile(false, decompressDir.getParent(), decompressDir);

            decompressTarget = volume.fromPath(decompressDir.toString());

            // creates the dest folder if not exists
            volume.createFolder(decompressTarget);

            // get the compress target list entry
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                final ZipArchiveEntry zipArchiveEntry = entries.nextElement();

                if (zipFile.canReadEntryData(zipArchiveEntry)) {
                    // get the entry infos
                    final String entryName = zipArchiveEntry.getName();
                    final InputStream archiveInputStream = zipFile.getInputStream(zipArchiveEntry);
                    final Target target = volume.fromPath(Paths.get(decompressDir.toString(), entryName).toString());
                    final Target parent = volume.getParent(target);

                    // create parent folder if not exists
                    if (parent != null && !volume.exists(parent)) {
                        volume.createFolder(parent);
                    }

                    if (!zipArchiveEntry.isDirectory()) {
                        // open streams to write the decompress target contents and auto close it
                        try (OutputStream outputStream = new BufferedOutputStream(
                                volume.openOutputStream(target))) {

                            // get entry content
//                            byte[] content = new byte[(int) zipArchiveEntry.getSize()];
//                            outputStream.write(content);
                            IOUtils.copy(archiveInputStream, outputStream);
                        }
                    }
                }
            }
        }
        return decompressTarget;
    }

    public static long crc32Checksum(byte[] bytes) {
        crc32.update(bytes);
        long checksum = crc32.getValue();
        crc32.reset();
        return checksum;
    }
}