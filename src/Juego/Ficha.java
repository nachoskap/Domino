/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import ClienteServidor.EspacioFicha;
import ClienteServidor.TableroPanel;
import static ClienteServidor.TableroPanel.listaEspacios;
import Control.Control;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author jesus
 */
public class Ficha extends JLabel implements Serializable, MouseListener, MouseMotionListener {

    int ladoA;
    int ladoB;
    //si es true, es que ya está ocupado ese lado
    boolean estadoA = false;
    boolean estadoB = false;
    boolean mula = false;
    boolean ponida = false;

    int grados = 0;
    /**
     * Identificador de objeto
     */
    private String key = "";
    /**
     * Posicion de imagen
     */
    int x = 0;
    int y = 0;
    //posicion original
    int x2 = 0;
    int y2 = 0;
    private Point posicion = new Point(x, y);
    /**
     * Tamaño de imagen
     */
    private Dimension d = new Dimension(78, 78);
    /**
     * variable que sirve para calcular el movimiento del objeto
     */
    private Point start_loc;
    /**
     * variable que sirve para calcular el movimiento del objeto
     */
    private Point start_drag;
    /**
     * variable que sirve para calcular el movimiento del objeto
     */
    private Point offset;
    /**
     * variables auxiliares para el desplazamiento del objeto
     */
    private int nuevo_X = 1;
    private int nuevo_Y = 1;

    int widthColi = 78;
    int heightColi = 40;
    String orientacion = "Horizontal";

    static int yano = 0;

    public Ficha(int ladoA, int ladoB) {
        this.ladoA = ladoA;
        this.ladoB = ladoB;
        this.setToolTipText("Ficha");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setSize(d);
        this.setPreferredSize(d);
        this.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Fichas/" + ladoA + "_" + ladoB + ".png")));
        this.setText("");
        this.setVisible(true);
        this.setLocation(posicion);
        //se agregan los listener
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        if (ladoA == ladoB) {
            mula = true;
        }

    }

    public int getLadoA() {
        return ladoA;
    }

    public void setLadoA(int ladoA) {
        this.ladoA = ladoA;
    }

    public int getLadoB() {
        return ladoB;
    }

    public void setLadoB(int ladoB) {
        this.ladoB = ladoB;
    }

    public boolean isEstadoA() {
        return estadoA;
    }

    public void setEstadoA(boolean estadoA) {
        this.estadoA = estadoA;
    }

    public boolean isEstadoB() {
        return estadoB;
    }

    public void setEstadoB(boolean estadoB) {
        this.estadoB = estadoB;
    }

    public boolean isMula() {
        return mula;
    }

    public void setMula(boolean mula) {
        this.mula = mula;
    }

    public boolean isPonida() {
        return ponida;
    }

    public void setPonida(boolean ponida) {
        this.ponida = ponida;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    @Override
    public String toString() {
        return "Ficha{" + "ladoA=" + ladoA + ", ladoB=" + ladoB + '}';
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Control.getJugador().isTurno()) {
            this.start_drag = getScreenLocation(e);
            this.start_loc = this.getLocation();
        } else {
            System.out.println("No es tu turno");
            this.setLugar(x2, y2);
        }

//        System.out.println("Pressed");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x = (this.getLocation().x);
        y = (this.getLocation().y);
        nuevo_X = (this.getLocation().x);
        nuevo_Y = (this.getLocation().y);
        this.setLocation(nuevo_X, nuevo_Y);
//para primera ficha del juego
        if (yano == 0) {
            if (TableroPanel.listaEspacios.get(0).getLadoDispA() == 5 && TableroPanel.listaEspacios.get(0).getLadoDispB() == 5) {
                if (this.getLadoA() == 5 && this.getLadoB() == 5) {
                    this.setLugar((int) TableroPanel.listaEspacios.get(0).getLocation().getX(), (int) TableroPanel.listaEspacios.get(0).getLocation().getY());

                    TableroPanel.listaEspacios.get(0).setOcupada(true);
                    TableroPanel.metodoPerron(listaEspacios.get(listaEspacios.indexOf(listaEspacios.get(0))));
                    Control.getTablero().getListaTablero().add(this);
                    Control.enviarMovimiento(new Movimiento(Control.getJugador(), Control.getTablero().getListaTablero()));
                    TableroPanel.listaEspacios.get(0).setOcupadoA(true);
                    TableroPanel.listaEspacios.get(0).setOcupadoB(true);
                    this.setPonida(true);
                    yano = 1;
                } else {
                    JOptionPane.showMessageDialog(null, "Debes inicar con la mula de 5");
                }

            }
        }

        for (int i = 0; i < TableroPanel.listaEspacios.size(); i++) {
            if (!this.isPonida()) {
                if (collision(TableroPanel.listaEspacios.get(i))) {

                    if (TableroPanel.listaEspacios.get(i).isOcupada()) {

                    } else {
                        System.out.println(this.getLadoA());
                        System.out.println(this.getLadoB());

                        System.out.println("Espacio");
                        System.out.println(TableroPanel.listaEspacios.get(i).getLadoDispA());
                        System.out.println(TableroPanel.listaEspacios.get(i).getLadoDispA());
                        if (TableroPanel.listaEspacios.get(i).getX() < (TableroPanel.tam.getWidth() / 2)) {
                            System.out.println("LadoA");
//                            System.out.println(TableroPanel.listaEspacios.get(i).getLadoDispA());
//                            System.out.println(TableroPanel.listaEspacios.get(i).getLadoDispB());
                            if (!TableroPanel.listaEspacios.get(i).isOcupadoA()) {
                                if (TableroPanel.listaEspacios.get(i).getLadoDispA() == this.getLadoA()) {

                                    this.setLugar((int) TableroPanel.listaEspacios.get(i).getLocation().getX(), (int) TableroPanel.listaEspacios.get(i).getLocation().getY());
                                    TableroPanel.listaEspacios.get(i).setOcupada(true);
                                    TableroPanel.listaEspacios.get(i).setLadoDispA(this.getLadoB());
                                    TableroPanel.listaEspacios.get(i).setLadoDispB(this.getLadoB());
                                    //aqui le movi
                                    TableroPanel.metodoPerron(listaEspacios.get(i));
                                    this.setPonida(true);

                                    TableroPanel.listaEspacios.get(i).setOcupadoB(true);

                                    rotar(TableroPanel.listaEspacios.get(i), "A");
                                    Control.getTablero().getListaTablero().add(this);
                                    Control.enviarMovimiento(new Movimiento(Control.getJugador(), Control.getTablero().getListaTablero()));

                                } else if (TableroPanel.listaEspacios.get(i).getLadoDispA() == this.getLadoB()) {
                                    this.setLugar((int) TableroPanel.listaEspacios.get(i).getLocation().getX(), (int) TableroPanel.listaEspacios.get(i).getLocation().getY());
                                    TableroPanel.listaEspacios.get(i).setOcupada(true);
                                    TableroPanel.listaEspacios.get(i).setLadoDispB(this.getLadoA());
                                    TableroPanel.listaEspacios.get(i).setLadoDispB(this.getLadoA());
                                    TableroPanel.metodoPerron(listaEspacios.get(i));
                                    this.setPonida(true);

                                    TableroPanel.listaEspacios.get(i).setOcupadoB(true);

                                    rotar(TableroPanel.listaEspacios.get(i), "B");
                                    Control.getTablero().getListaTablero().add(this);
                                    Control.enviarMovimiento(new Movimiento(Control.getJugador(), Control.getTablero().getListaTablero()));

                                }
                            }
                        } else {
                            System.out.println("LadoB");
                            if (!TableroPanel.listaEspacios.get(i).isOcupadoB()) {
                                if (TableroPanel.listaEspacios.get(i).getLadoDispB() == this.getLadoA()) {
                                    this.setLugar((int) TableroPanel.listaEspacios.get(i).getLocation().getX(), (int) TableroPanel.listaEspacios.get(i).getLocation().getY());
                                    TableroPanel.listaEspacios.get(i).setOcupada(true);
                                    TableroPanel.listaEspacios.get(i).setLadoDispA(this.getLadoB());
                                    TableroPanel.listaEspacios.get(i).setLadoDispA(this.getLadoB());
                                    TableroPanel.metodoPerron(listaEspacios.get(i));
                                    this.setPonida(true);
                                    TableroPanel.listaEspacios.get(i).setOcupadoA(true);
//                                    this.rotar(190);
                                    rotar(TableroPanel.listaEspacios.get(i), "A");
                                    Control.getTablero().getListaTablero().add(this);
                                    Control.enviarMovimiento(new Movimiento(Control.getJugador(), Control.getTablero().getListaTablero()));
                                } else if (TableroPanel.listaEspacios.get(i).getLadoDispB() == this.getLadoB()) {
                                    this.setLugar((int) TableroPanel.listaEspacios.get(i).getLocation().getX(), (int) TableroPanel.listaEspacios.get(i).getLocation().getY());
                                    TableroPanel.listaEspacios.get(i).setOcupada(true);
                                    TableroPanel.listaEspacios.get(i).setLadoDispA(this.getLadoB());
                                    TableroPanel.listaEspacios.get(i).setLadoDispA(this.getLadoB());
                                    TableroPanel.metodoPerron(listaEspacios.get(i));
                                    this.setPonida(true);
                                    TableroPanel.listaEspacios.get(i).setOcupadoA(true);
                                    rotar(TableroPanel.listaEspacios.get(i), "B");
                                    Control.getTablero().getListaTablero().add(this);
                                    Control.enviarMovimiento(new Movimiento(Control.getJugador(), Control.getTablero().getListaTablero()));

                                }
                            }
                        }

//                    System.out.println("Colision");
//                    TableroPdanel.listaEspacios.get(i).setOcupada(true);
//                    this.setLugar((int) TableroPanel.listaEspacios.get(i).getLocation().getX(), (int) TableroPanel.listaEspacios.get(i).getLocation().getY());
//                        if (TableroPanel.listaEspacios.get(i).getOrientacion().equalsIgnoreCase("Vertical")) {
////                        this.rotar(90);
//                        } else {
//                            this.rotar(90);
//                        }
                    }
//                    if (this.getLocation() != TableroPanel.listaEspacios.get(i).getLocation()) {
//                        this.setLugar(this.x2, this.y2);
//                    }

                }
            } else {
//                System.out.println("no se puede puto");
            }
//           
        }

//        if (collision(Control.getJugador().getListaFichas().get(0))) {
//            System.out.println("Colision carnal");
//        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 0, 51), 1));

    }

    @Override
    public void mouseExited(MouseEvent e) {

        this.setBorder(null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!this.isPonida()) {
            if (Control.getJugador().isTurno()) {
                Point current = this.getScreenLocation(e);
                offset = new Point((int) current.getX() - (int) start_drag.getX(), (int) current.getY() - (int) start_drag.getY());
                Point new_location = new Point((int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc.getY() + offset.getY()));
                this.setLocation(new_location);
                this.x = (int) offset.getX();
                this.y = (int) offset.getY();
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private Point getScreenLocation(MouseEvent evt) {
        Point cursor = evt.getPoint();
        Point target_location = this.getLocationOnScreen();
        return new Point((int) (target_location.getX() + cursor.getX()),
                (int) (target_location.getY() + cursor.getY()));
    }

    public void rotar(int grados) {
        this.widthColi = 40;
        this.heightColi = 78;
        this.orientacion = "Vertical";
//        this.x = x + (this.getWidth() / 4);
//        this.y = y - (this.getHeight() / 4);
        this.grados = grados;

        repaint();
    }

    public void rotar(EspacioFicha ef, String disponible) {
        System.out.println(ef.getOrientacion());
        System.out.println(this.getOrientacion());
        if (ef.getOrientacion().equalsIgnoreCase("Vertical")) {
            if (this.getOrientacion().equalsIgnoreCase("Horizontal")) {
                this.rotar(90);
            }
        } else {
            if (this.getOrientacion().equalsIgnoreCase("Vertical")) {

                if (ef.getLadoDisponible().equalsIgnoreCase("A")) {
                    if (disponible.equalsIgnoreCase("A")) {
                        this.rotar(180);
                    } else {
                        this.rotar(360);
                    }

                } else {
                    if (disponible.equalsIgnoreCase("A")) {
                        this.rotar(360);
                    } else {
                        this.rotar(180);
                    }
                }

            }

        }
    }

    public void paintComponent(Graphics g) {
        double r = Math.toRadians(grados); //se convierte a radianes lo grados

        Graphics2D gx = (Graphics2D) g;

        gx.rotate(r, getWidth() / 2, getHeight() / 2);
//        this.setBorder(BorderFactory.createLineBorder(new java.awt.Color(204, 0, 51), 1));
        super.paintComponent(g);

    }

    public void setLugar(int x, int y) {
        this.x = x;
        this.y = y;
        this.x2 = x;
        this.y2 = y;
//        this.y = y + ((this.getHeight() / 2) - 20);
        setLocation(x, y);

    }

    public boolean collision(EspacioFicha f) {
        return f.getBounds().intersects(getBounds());

    }

    public boolean collision(Ficha f) {
        return f.getBounds().intersects(getBounds());

    }

    public Rectangle getBounds() {
        if (this.orientacion.equalsIgnoreCase("Horizontal")) {
            return new Rectangle(x, y - ((this.getHeight() / 2) - 20), widthColi, heightColi);
        } else {
            return new Rectangle(x + ((this.getWidth() / 2) - 39), y, widthColi, heightColi);
        }

    }
}
