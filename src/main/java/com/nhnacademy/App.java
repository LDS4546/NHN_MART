package com.nhnacademy;

import com.nhnacademy.customer.generator.CustomerGenerator;
import com.nhnacademy.nhnmart.entring.EnteringQueue;
import com.nhnacademy.nhnmart.product.parser.ProductParser;
import com.nhnacademy.nhnmart.product.parser.impl.CsvProductParser;
import com.nhnacademy.nhnmart.product.repository.ProductRepository;
import com.nhnacademy.nhnmart.product.repository.impl.MemoryProductRepository;
import com.nhnacademy.nhnmart.product.service.ProductService;
import com.nhnacademy.nhnmart.product.service.impl.ProductServiceImpl;
import com.nhnacademy.thread.ThreadPool;
import com.nhnacademy.thread.customer.CustomerShoppingHandler;
import com.nhnacademy.thread.util.RequestChannel;
import com.nhnacademy.thread.util.RequestHandler;

public class App
{
    public static void main( String[] args )
    {
        //capacity를 100으로 enteringQueue를 초기화 합니다.
        EnteringQueue enteringQueue = new EnteringQueue(100);

        //customerGenerator를 이용해서 thread를 생성 합니다.
        CustomerGenerator customerGenerator = new CustomerGenerator(enteringQueue);
        Thread enteringThread = new Thread(customerGenerator);

        //enteringThread의 이름을 'entering-thread'로 설정, enteringThread를 시작 합니다.
        enteringThread.setName("entering-thread");
        enteringThread.start();

        //MemoryProductRepository 구현체를 이용해서 ProductRepository 객체를 생성 합니다.
        ProductRepository productRepository = new MemoryProductRepository();
        //CsvProductParser 구현체를 이용해서 ProductParser 객체를 생성 합니다.
        ProductParser productParser = new CsvProductParser();
        //ProductServiceImpl 구현체를 이용해서 ProductService 객체를 생성 합니다.
        ProductService productService = new ProductServiceImpl(productRepository,productParser);



        /*
            쇼핑은 동시에 최대 10명까지 할 수 있습니다.
            CartStore에서 cart를 대여 한다.
            cart를 대여 후 쇼핑을 한다.
            쇼핑이 완료되면 계산을 할 수 있도록 대기 한다.
        */

        //checkout 대기열의 queueSize : 20으로 설정 합니다.
        RequestChannel checkoutChannel = null;

        //shoppingThreadPool, poolSize=10 생성후 실행 합니다.
        Runnable customerRunnable = null;
        ThreadPool shoppingThreadPool = null;


        //checkout을 하기위한 threadPool을 생성 합니다. poolSize =3 , 즉 동시에 3군대서 계산을 진행할 수 있습니다.
        RequestHandler requestHandler = null;
        ThreadPool checkOutThreadPool = null;

        // 60초 후 종료 됩니다.
        // enteringThread, shoppingThreadPool, checkOutThreadPool
        
    }
}
