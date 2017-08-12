/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;

/**
 *
 * @author Сергей
 */
public class PaintPanel extends JPanel implements ComponentListener {
    
      private final Terrain terrain;
      final int width = 700, height = 700;
      
    public PaintPanel() {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        addComponentListener(PaintPanel.this);
        terrain = new Terrain();
        regenerateTerrain();
    }
    
    public void generateNew() {
        regenerateTerrain();
        repaint();
    }
    
    public void setSmoothIterations(int smoothIterations) {
       terrain.setRun(smoothIterations);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        terrain.draw(g);
    }
    
    private void regenerateTerrain() {
        terrain.generate(System.currentTimeMillis());
    }

    @Override
    public void componentResized(ComponentEvent e) {
      //  terrain.setSize(e.getComponent().getWidth(), e.getComponent().getHeight());
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
