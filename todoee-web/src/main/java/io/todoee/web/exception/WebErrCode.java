package io.todoee.web.exception;

import io.todoee.web.constants.WebConstant;

/**
 * 
 * @author James.zhang
 *
 */
public class WebErrCode {
	public static final String CONTENT_TYPE_EMPTY = WebConstant.MODULE + ".content-type.empty";
	public static final String CONTENT_TYPE_INVALID = WebConstant.MODULE + ".content-type.invalid";
	
	public static final String REQUEST_PARAM_REQUIRED = WebConstant.MODULE + ".request-param.required";
	
	public static final String REQUEST_PARAM_ADAPT_ERROR = WebConstant.MODULE + ".request-param.adapt.error";
	public static final String REQUEST_PARAMS_ADAPT_ERROR = WebConstant.MODULE + ".request-params.adapt.error";
	
	public static final String FORM_ATTRIBUTE_ADAPT_ERROR = WebConstant.MODULE + ".form-attribute.adapt.error";
	public static final String FORM_ATTRIBUTES_ADAPT_ERROR = WebConstant.MODULE + ".form-attributes.adapt.error";
	
	public static final String HEADER_PARAM_REQUIRED = WebConstant.MODULE + ".header-param.required";
	public static final String HEADER_PARAM_ADAPT_ERROR = WebConstant.MODULE + ".header-param.adapt.error";
	
	public static final String PATH_PARAM_REQUIRED = WebConstant.MODULE + ".path-param.required";
	public static final String PATH_PARAM_ADAPT_ERROR = WebConstant.MODULE + ".path-param.adapt.error";
	
	public static final String INTERNAL_SERVER_ERROR = WebConstant.MODULE + ".internal.server.error";
}
