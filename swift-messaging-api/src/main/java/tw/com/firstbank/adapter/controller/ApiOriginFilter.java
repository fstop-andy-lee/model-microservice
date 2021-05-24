package tw.com.firstbank.adapter.controller;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen",
    date = "2021-05-22T12:54:58.959Z[GMT]")
public class ApiOriginFilter implements javax.servlet.Filter {

  @SuppressWarnings("unused")
  private FilterConfig filterConfig;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse res = (HttpServletResponse) response;
    res.addHeader("Access-Control-Allow-Origin", "*");
    res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    res.addHeader("Access-Control-Allow-Headers", "Content-Type");
    //TODO change to authorization
    // res.setHeader("Access-Control-Allow-Headers", "content-type, authorization");
    
    HttpServletRequest req = (HttpServletRequest) request;
    if (!req.getMethod().equals("OPTIONS")) {
      chain.doFilter(request, response);

    } else {
      res.setStatus(HttpServletResponse.SC_OK);
    }

  }

  @Override
  public void destroy() {}

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
  }
}
