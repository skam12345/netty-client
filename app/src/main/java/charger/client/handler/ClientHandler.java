package charger.client.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import charger.client.constant.Constant;
import charger.client.parameter.HeartBeatParameter;
import charger.client.parameter.LoginParameter;
import charger.client.parameter.StartParameter;
import charger.client.parameter.StopParameter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.SystemPropertyUtil;


public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private Boolean isLogin = true;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit((Runnable) () -> {
            while(isLogin) {
                ByteBuf writeBuf = Unpooled.directBuffer();
                writeBuf.writeBytes(new LoginParameter().login());
                ChannelFuture cf = ctx.writeAndFlush(writeBuf);
                cf.addListener(new ChannelFutureListener() {
                    
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(future.isSuccess()) {
                            System.out.println("??s");
                        }else {
                            System.out.println("??s");
                        }
                    } 
                });
                try {Thread.sleep(20000); } catch(InterruptedException e) { e.printStackTrace(); };
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        ByteBuf buff = (ByteBuf) msg;
        String bbu = ByteBufUtil.hexDump(buff).toUpperCase();
        if(bbu.length() == 30 && bbu.toString().contains("6821") && bbu.toString().contains(Constant.chargerId)) {
            System.out.println("로그인 완료");
            isLogin = false;
            ExecutorService heartBeat = Executors.newFixedThreadPool(1);
            heartBeat.execute(() -> {
                while(true) {
                    ByteBuf writeBuf = Unpooled.directBuffer();
                    writeBuf.writeBytes(new HeartBeatParameter().heartbeat());
                    ChannelFuture cf = ctx.writeAndFlush(writeBuf);
                    cf.addListener(new ChannelFutureListener() {
                        
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if(future.isSuccess()) {
                                
                            }
                        } 
                    });
                    try {Thread.sleep(5000); } catch(InterruptedException e) { e.printStackTrace(); }
                }
            });
        }else if(bbu.length() == 30 && bbu.toString().contains("6824") && bbu.toString().contains(Constant.chargerId)) {
        }else if(bbu.length() == 122 && bbu.toString().substring(34, 36).equals("02") && bbu.toString().contains(Constant.chargerId)) {
            System.out.println("충전 시작");
            ByteBuf writeBuf = Unpooled.directBuffer();
            writeBuf.writeBytes(new StartParameter().start());
            ChannelFuture cf = ctx.writeAndFlush(writeBuf);
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()) {
                        
                    }
                } 
            });
        }else if(bbu.length() == 122 && bbu.toString().substring(34, 36).equals("03") && bbu.toString().contains(Constant.chargerId)) {
            System.out.println("충전 중지");
            ByteBuf writeBuf = Unpooled.directBuffer();
            writeBuf.writeBytes(new StopParameter().stop());
            ChannelFuture cf = ctx.writeAndFlush(writeBuf);
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()) {
                        
                    }
                } 
            });
        }
    }
}
