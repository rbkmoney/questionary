package com.rbkmoney.questionary.endpoint;

import com.rbkmoney.questionary.manage.QuestionaryManagerSrv;
import com.rbkmoney.woody.thrift.impl.http.THServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/questionary")
public class QuestionaryManagerServlet extends GenericServlet {

    private Servlet thriftServlet;

    @Autowired
    private QuestionaryManagerSrv.Iface requestHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        thriftServlet = new THServiceBuilder().build(QuestionaryManagerSrv.Iface.class, requestHandler);
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        thriftServlet.service(request, response);
    }
}
