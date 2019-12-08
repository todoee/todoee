package io.meedo.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.meedo.finder.support.config.ElfinderRootConfig;
import io.meedo.finder.web.controller.ConnectorController;

/**
 * ConnectorServlet is an example servlet it creates a ConnectorController on
 * init() and use it to handle requests on doGet()/doPost()
 * 
 * users should extend from this servlet and customize required protected
 * methods
 * 
 * @author James.zhang
 *
 */
public class ConnectorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private ConnectorController _connectorController;

	/**
	 * create a connector controller
	 * 
	 * @param config
	 * @return
	 */
	protected ConnectorController createConnectorController(ServletConfig config) {
		ConnectorController connectorController = new ConnectorController();

		ElfinderRootConfig elfinderRootConfig = new ElfinderRootConfig();
		connectorController.setElfinderCommandFactory(elfinderRootConfig.getCommandFactory());
//		connectorController.setElfinderStorageFactory(elfinderRootConfig.getElfinderStorageFactory());
		
		return connectorController;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_connectorController.connector(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		_connectorController.connector(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		_connectorController = createConnectorController(config);
	}
}
