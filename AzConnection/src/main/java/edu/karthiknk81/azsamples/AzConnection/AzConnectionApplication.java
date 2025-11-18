package edu.karthiknk81.azsamples.AzConnection;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AzConnectionApplication {

	public static void main(String[] args) {

        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        AzRMConnector rm = (AzRMConnector) xmlContext.getBean("azMgr");
        rm.printResourceGroups(); 
	}

}
