package com.jamaskii.core.alert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class ImageAlert extends Frame{

    public ImageAlert(String title,byte[] imageData) {

        // 默认的窗体名称
        this.setTitle(title);


        // 获得面板的实例
        ImagePanel panel = new ImagePanel(imageData);
        this.add(panel);
        this.addWindowListener(new WindowAdapter() {
            //设置关闭
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // 执行并构建窗体设定
        this.pack();
    }

    public void display(){
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}

class ImagePanel extends Panel {

    private final Image screenImage = new BufferedImage(800, 600, 2);

    private final Graphics2D screenGraphic = (Graphics2D) screenImage.getGraphics();

    private Image backgroundImage;

    public ImagePanel(byte[] imageData) {
        try{
            //加载图片
            BufferedImage bufferedImage= ImageIO.read(new ByteArrayInputStream(imageData));
            backgroundImage=bufferedImage;
            // 设定焦点在本窗体
            setFocusable(true);

            // 设定初始构造时面板大小,这里先采用图片的大小
            int width=bufferedImage.getWidth();
            int height=bufferedImage.getHeight();
            int x=0;
            int y=0;
            if(width<196){
                width=196;
                x=98-(bufferedImage.getWidth()/2);
            }
            if(height<64){
                height=64;
                y=32-(bufferedImage.getHeight()/2);
            }

            setPreferredSize(new Dimension(width,height));
            // 绘制背景
            drawView(x,y);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void drawView(int x,int y) {
        screenGraphic.drawImage(backgroundImage, x, y, null);
    }

    public void paint(Graphics g) {
        g.drawImage(screenImage, 0, 0, null);
    }

}