/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.util;

import com.cn.webService.client.GpsWebService;


/**
 *
 * @author LFeng
 */
public class CarNumberVerifie {

    /**
     * 验证系统中是否存在该车牌号
     *
     * @param paramValue
     * @return 存在则返回车牌号对应系统编号数组[{"VehNof":"晋A174A8","SystemNo":"13100011470"}]
     * 不存在则返回null
     */
    /*
    public static String vehnofVerifie(String paramValue) {
        try {
            String webServiceAddress = "http://221.204.213.71/zygpsservice/gpswebservice.asmx?WSDL";
            String webServiceSoapAction = "http://tempuri.org/VehnofVerifie";
            String webServiceNamespace = "http://tempuri.org/";
            String webServiceMethodName = "VehnofVerifie";
            String webServiceParamName = "vehnof";
            
            ServiceClient client = new ServiceClient();
            Options options = new Options();
            EndpointReference reference = new EndpointReference(webServiceAddress);
            options.setTo(reference);
            options.setAction(webServiceSoapAction);
            options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
            client.setOptions(options);
            
            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMNamespace mNamespace = factory.createOMNamespace(webServiceNamespace, "");
            OMElement method = factory.createOMElement(webServiceMethodName, mNamespace);
            OMElement value = factory.createOMElement(webServiceParamName, mNamespace);
            value.setText(paramValue);
            method.addChild(value);
            OMElement result = client.sendReceive(method);
            return result.getFirstElement().getText();
        } catch (AxisFault ex) {
            Logger.getLogger(CarNumberVerifie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    */
    /**
     * 
     * @param paramValue
     * @return
     * @throws AxisFault 
     */
    /*
    public static String vehnofVerifieRPC(String paramValue) throws AxisFault {
        String webServiceAddress = "http://221.204.213.71/zygpsservice/gpswebservice.asmx";
        String webServiceSoapAction = "http://tempuri.org/VehnofVerifie";
        String webServiceNamespace = "http://tempuri.org/";
        String webServiceMethodName = "VehnofVerifie";
        String webServiceParamName = "vehnof";

        Object[] opArgs = new Object[]{paramValue};
        Class<?>[] opReturnType = new Class[]{String.class};
        RPCServiceClient serviceClient = new RPCServiceClient();
        Options options = serviceClient.getOptions();
        EndpointReference reference = new EndpointReference(webServiceAddress);
        options.setTo(reference);
        options.setAction(webServiceSoapAction);
        options.setManageSession(true);
        options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT,true);
        options.setTimeOutInMilliSeconds(60000L);
        
        QName qName = new QName(webServiceNamespace, webServiceMethodName);
        String result = (String) serviceClient.invokeBlocking(qName, opArgs, opReturnType)[0];
        serviceClient.cleanupTransport();
        return result;
    }
    */
    
    /**
     * 验证系统中是否存在该车牌号
     *
     * @param paramValue
     * @return 存在则返回车牌号对应系统编号数组[{"VehNof":"晋A174A8","SystemNo":"13100011470"}]
     * 不存在则返回null
     */
    public static String vehnofVerifie(String paramValue) {
        return new GpsWebService().getGpsWebServiceSoap().vehnofVerifie(paramValue);
    }
    
    
    public static void main(String[] args) {
        /*
        System.out.println(vehnofVerifie("陕KC6064"));
        */
    }    
}
