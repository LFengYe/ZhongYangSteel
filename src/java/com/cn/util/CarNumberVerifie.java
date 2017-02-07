/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.util;

import com.cn.webService.client.GpsWebService;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    
    /*
    public static void main(String[] args) {
        try {
            String test = "FbK4Ezk6mad8qPV3GUxF0wjp+jVsrjyhY+eb7Gy675WT58nug5deBDeqz2ss45FV3jaWkX5koFN6Ikr9Cx+XZprhAb2V2uCLicHVbcHFjkntbd2JyIlYL23G5PdqWJh1Lt/MMbwpBB5TVkFmtAOQzuPIb+D33aAtEpghcbQN6XniHfXSL7SxyaGqibMgbqZHLwiQamVnPOmuR/Fp+nMQdCtbhTm0FigyY2h7dH9IQJJSf5mfv/KU5A==";
//            String test = "{\"username\":\"a33102a234ee7072269d3206b8618c69\",\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"type\":\"enterprise\",\"IMEI\":\"102AF67CA6D24B3C8EF991FBD9306BFE\"}";
            System.out.println(EncryptUtil.decryptDES(test));
        } catch (Exception ex) {
            Logger.getLogger(CarNumberVerifie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
}
