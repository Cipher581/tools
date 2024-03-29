package art.cipher581.tools.pixelart.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import art.cipher581.commons.gui.ImagePanel;
import art.cipher581.tools.pixelart.GlobalModel;
import art.cipher581.tools.pixelart.core.Pixel;
import art.cipher581.tools.pixelart.core.Project;

public class ProjectImagePanel extends JPanel {

    /**
	 * SVUID
	 */
	private static final long serialVersionUID = -8513514202953324233L;

	private BufferedImage image;

    private ImagePanel imagePanel;

    private ImageModifier imageModifier = new ImageModifier();

    /**
     * Creates new form ProjectImagePanel
     */
    public ProjectImagePanel(BufferedImage image) {
        super();

        this.image = image;

        initComponents();

        int zoom = GlobalModel.getGuiConfiguration().getZoom();
        setZoom(zoom);

        imagePanel.addMouseMotionListener(createMouseMotionListener());
        imagePanel.addMouseListener(createMouseClickListener());
    }
    
    
    private MouseMotionListener createMouseMotionListener() {
        return new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();

                int pX = (int) p.getX();
                int pY = (int) p.getY();

                int x = pX / imageModifier.getZoom();
                int y = pY / imageModifier.getZoom();

                Pixel pixel = getPixel(p);

                if (pixel == null) {
                    jLabelInfo.setText("");
                } else {
                    String doneInfo;
                    if (GlobalModel.getProject().isDone(pixel)) {
                        doneInfo = "done";
                    } else {
                        doneInfo = "not done";
                    }

                    jLabelInfo.setText("x: " + (x + 1) + "; y: " + (y + 1) + "; " + doneInfo);
                }
            }

        };
    }
    
    private MouseListener createMouseClickListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    Project project = GlobalModel.getProject();

                    Point p = e.getPoint();

                    Pixel pixel = getPixel(p);

                    if (pixel != null) {
                        project.setCurrent(pixel);
                    }
                }
            }

        };
    }

    private Pixel getPixel(Point p) {
        int pX = (int) p.getX();
        int pY = (int) p.getY();

        int x = pX / imageModifier.getZoom();
        int y = pY / imageModifier.getZoom();

        Project project = GlobalModel.getProject();

        Pixel pixel = project.getPixel(x, y);

        return pixel;
    }

    public void setZoom(int zoom) {
        imageModifier.setZoom(zoom);

        BufferedImage newImage = imageModifier.modify(image);

        showImage(newImage);

        GlobalModel.getGuiConfiguration().setZoom(zoom);
    }

    private void showImage(BufferedImage image) {
        System.out.println("showImage");
        if (imagePanel != null) {
            jPanelImageContainer.remove(imagePanel);
        }

        this.imagePanel = new ImagePanel(image, null);

        imagePanel.setSize(new Dimension(image.getWidth(), image.getHeight()));
        imagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        imagePanel.setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
        imagePanel.setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));

        jPanelImageContainer.add(imagePanel);

        revalidate();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelButtons = new javax.swing.JPanel();
        jButtonZoomIn = new javax.swing.JButton();
        jButtonZoomOut = new javax.swing.JButton();
        jPanelInfos = new javax.swing.JPanel();
        jLabelInfo = new javax.swing.JLabel();
        jScrollPaneImage = new javax.swing.JScrollPane();
        jPanelImageContainer = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanelButtons.setLayout(new java.awt.GridBagLayout());

        jButtonZoomIn.setText("Zoom +");
        jButtonZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomInActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelButtons.add(jButtonZoomIn, gridBagConstraints);

        jButtonZoomOut.setText("Zoom -");
        jButtonZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonZoomOutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelButtons.add(jButtonZoomOut, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        add(jPanelButtons, gridBagConstraints);

        jPanelInfos.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanelInfos.add(jLabelInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanelInfos, gridBagConstraints);

        jPanelImageContainer.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPaneImage.setViewportView(jPanelImageContainer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPaneImage, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomInActionPerformed
        zoomIn();
    }//GEN-LAST:event_jButtonZoomInActionPerformed

    private void jButtonZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonZoomOutActionPerformed
        zoomOut();
    }//GEN-LAST:event_jButtonZoomOutActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonZoomIn;
    private javax.swing.JButton jButtonZoomOut;
    private javax.swing.JLabel jLabelInfo;
    private javax.swing.JPanel jPanelButtons;
    private javax.swing.JPanel jPanelImageContainer;
    private javax.swing.JPanel jPanelInfos;
    private javax.swing.JScrollPane jScrollPaneImage;
    // End of variables declaration//GEN-END:variables

    private void zoomIn() {
        int zoom = imageModifier.getZoom();

        setZoom(zoom + 1);
    }

    private void zoomOut() {
        int zoom = imageModifier.getZoom();

        if (zoom > 1) {
            setZoom(zoom - 1);
        }
    }
}
