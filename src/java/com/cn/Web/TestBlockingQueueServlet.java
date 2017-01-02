/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.Web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author LFeng
 */
public class TestBlockingQueueServlet extends HttpServlet {

    AtomicInteger count = new AtomicInteger();
    BlockingQueue<HttpServletResponse> queue = new LinkedBlockingQueue<>();
    boolean isRunning = false;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestBlockingQueueServlet() {
        super();
        // TODO Auto-generated constructor stub  
    }

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
        int currentCount = +count.incrementAndGet();
        int data = currentCount;
        if (queue.offer(response)) {
            System.out.println("第" + currentCount + "个请求插入队列成功！"+ response.toString());
        } else {
            System.out.println("第" + currentCount + "个请求插入队列失败！");
        }

        if (!isRunning) {
            Thread doSomethingThread = new Thread(new DoSomething());
            doSomethingThread.start();
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

    class DoSomething implements Runnable {

        @Override
        public void run() {
            isRunning = true;
            // TODO Auto-generated method stub  
            try {
                while (isRunning) {
//                    String data = queue.poll(2, TimeUnit.SECONDS);//2秒内取不到就返回null,也可以用take方法，取不到数据会阻塞，isRunning可以不用
                    HttpServletResponse response = queue.poll();
                    if (null != response) {
                        System.out.println("正在处理请求！" + response.toString());
                        Thread.sleep(10000);
                        PrintWriter out = response.getWriter();
                        response.setContentType("text/html;charset=UTF-8");
                        response.setHeader("Cache-Control", "no-store");
                        response.setHeader("Pragma", "no-cache");
                        response.setDateHeader("Expires", 0);
                        out.print("test");
                    } else {
                        System.out.println("队列里没数据！");
                        // 超过2s还没数据，自动退出线程。  
                        isRunning = false;
                    }
                }
            } catch (InterruptedException e) {
            } catch (IOException ex) {
                Logger.getLogger(TestBlockingQueueServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
