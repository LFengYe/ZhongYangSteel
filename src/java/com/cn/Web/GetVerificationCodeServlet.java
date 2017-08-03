/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.Web;

import com.cn.util.VerificationCodeTool;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 *
 * @author LFeng
 */
public class GetVerificationCodeServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(GetVerificationCodeServlet.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //verification code demander  
        //String vCdemander = request.getParameter("vcdemander");
//        System.out.println("GetVerificationCodeServlet");
        try {
            //set encoding  
            request.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            //Verification code tool  
            VerificationCodeTool vcTool = new VerificationCodeTool();
            BufferedImage image = vcTool.drawVerificationCodeImage();
            //Verification code result  
            int vcResult = vcTool.getXyresult();
            String vcValue = vcTool.getRandomString();
            //Set ban cache  
            //Cache-control : Specify the request and response following caching mechanism  
            //no-cache: Indicates a request or response message cannot cache  
            response.setHeader("Cache-Control", "no-cache");
            //Entity header field response expiration date and time are given  
            response.setHeader("Pragma", "No-cache");
            response.setDateHeader("Expires", 0);
            // Set the output stream content format as image format  
            response.setContentType("image/png");
            //session  
            //true: if the session exists, it returns the session, or create a new session.  
            //false: if the session exists, it returns the session, otherwise it returns null.  
            HttpSession session = request.getSession(true);
            session.setAttribute("imageCode", vcResult);
            //set verificationcode to session
            /*
            //login  
            if ("userlogin".equals(vCdemander)) {
                session.setAttribute("verificationcodeuserlogin", vcResult);
            }
            //regiser  
            if ("userregister".equals(vCdemander)) {
                session.setAttribute("verificationcodeuserregister", vcResult);
            }
            */
            //To the output stream output image  
            ImageIO.write(image, "png", response.getOutputStream());
            //LOG.info("获取验证码成功 :\n验证码:" + vcValue + "\n验证码结果:" + vcResult);
        } catch (Exception e) {
            //LOG.error("获取验证码失败", e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
