/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.Board;
import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.GameStatus;
import edu.upc.epsevg.prop.loa.IAuto;
import edu.upc.epsevg.prop.loa.IPlayer;
import edu.upc.epsevg.prop.loa.Move;
import edu.upc.epsevg.prop.loa.SearchType;
import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author aibar
 */
public class NotCheckers implements IPlayer, IAuto {

    //Globales
    private String name;
    private GameStatus gs;
    Boolean TimeOut = false;
    Boolean TimeOutWinners = false;
    int prof = 8;
    int infneg = -99999999;    //-inf
    int infpos = 99999999;     // +inf
    int cont = 0;             //contador dels nodes explorats (OJO HAY UNA FUNCIÓN EN Move que te lo dice)
    ArrayList<Point> posiciones;

//-------------------------------
    public NotCheckers(String name, int profunditat) {
        this.name = name;
        prof = profunditat;
    }

    @Override
    public Move move(GameStatus gs) {
        int valor = infneg;
        int alfa = infneg;
        int beta = infpos;
        TimeOut = false;
        TimeOutWinners = false;
        posiciones = new ArrayList<Point>();
        int millorMov = 0;  //Millor pos. on es possarà les fitxes
        int heu = 0;        //Resultat de l'heuristica
        CellType color = gs.getCurrentPlayer();
        int profunditat = prof;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        Move movi = new Move(PosFrom, PosTo, 0, 0, SearchType.MINIMAX);
        Move auxMovi = new Move(PosFrom, PosTo, 0, 0, SearchType.MINIMAX);
        for (int depth = 0; (!TimeOut) && depth<profunditat ; depth++) {
            for (int i = 0; (!TimeOut) && i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if ((!TimeOut) && !points.isEmpty()) { //si nos podemos mover
                    for (int mov = 0; (!TimeOut) && mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux = new GameStatus(gs);
                        aux.movePiece(PosFrom, PosTo);
                        CellType colorRival = color.opposite(color);
                        heu = min_Valor(aux, colorRival, alfa, beta, profunditat - 1);
                        if (valor <= heu) {
                            millorMov = mov;
                            valor = heu;
                            movi = new Move(PosFrom, PosTo, cont, profunditat, SearchType.MINIMAX);
                        }
                    }
                }
            }
            if (!TimeOut || !TimeOutWinners) {
                auxMovi = movi;
            }
        }
        System.out.println("AuxMovi: "+auxMovi.getFrom()+""+auxMovi.getTo());
        //System.out.println("mensaje" + heuristica(gs, color));
        return auxMovi;
    }

    public int min_Valor(GameStatus gs, CellType color, int alfa, int beta, int profunditat) {
        CellType colorRival = color.opposite(color); //color del contrario -*-=+
        int valor = 0;
        cont++;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        if (TimeOut) return 6;
        if (gs.isGameOver()) {
            if (gs.GetWinner() == colorRival) {
                TimeOutWinners = true;
                return infpos;
            }

        } else if (profunditat == 0) {
            return heuristica(gs, colorRival);

        } else {
            valor = infpos;
            for (int i = 0; (!TimeOut) && i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if ((!TimeOut) && points.size() != 0) { //si nos podemos mover
                    for (int mov = 0;(!TimeOut) && mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux2 = new GameStatus(gs);
                        aux2.movePiece(PosFrom, PosTo);
                        //if (TimeOut) return 6;
                        valor = Integer.min(valor, max_Valor(aux2, colorRival, alfa, beta, profunditat - 1));
                        beta = Integer.min(valor, beta);
                        if (beta <= alfa) {
                            return valor;
                        }
                    }
                }
            }
        }
        return valor;
    }

    public int max_Valor(GameStatus gs, CellType color, int alfa, int beta, int profunditat) {
        CellType colorRival = color.opposite(color); //color del contrario -*-=+
        int valor = 0;
        cont++;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        if (TimeOut) return 6;
        if (gs.isGameOver()) {
            if (gs.GetWinner() == colorRival) {
                TimeOutWinners = true;
                return infneg;  
            }
        } else if (profunditat == 0) {
            return heuristica(gs, color);

        } else {
            valor = infneg;
            for (int i = 0; (!TimeOut) && i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if ((!TimeOut) && points.size() != 0) { //si nos podemos mover
                    for (int mov = 0; (!TimeOut) && mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux2 = new GameStatus(gs);
                        aux2.movePiece(PosFrom, PosTo);
                        //if (TimeOut) return 6;
                        valor = Integer.max(valor, min_Valor(aux2, colorRival, alfa, beta, profunditat - 1));
                        alfa = Integer.max(valor, alfa);
                        if (beta <= alfa) {
                            return valor;
                        }
                    }
                }
            }
        }
        return valor;
    }
    
    @Override
    public void timeout() {
        TimeOut = true;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean ComprobarPosicion(GameStatus gs, int fila, int columna) {
        boolean posicion = false;
        if (fila >= 0 && fila < gs.getSize() && columna >= 0 && columna < gs.getSize()) {
            posicion = true;
        }

        return posicion;
    }

    public int heuristica(GameStatus gs, CellType color) {

        int rival = 0;
        int nuestras = 0;
        CellType colorRival = color.opposite(color);
        rival = Nagrupadas(gs, colorRival);
        nuestras = Nagrupadas(gs, color);

        return nuestras - rival;

    }

    public int Nagrupadas(GameStatus gs, CellType color) {   //numero de fitxes afrupades al tauler
        int FichasTablero = gs.getNumberOfPiecesPerColor(color);
        int contador[] = new int[12];
        int medio = 0;
        ArrayList<Point> Num_agrupadas = new ArrayList<Point>();
        for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
            Point Pos = gs.getPiece(color, i);
            Num_agrupadas.add(Pos);
        }
        for (Point Pos : Num_agrupadas) {
            posiciones.clear();
            Agrupadas(gs, color, Pos.x, Pos.y);
            medio = Enmedio(Pos);
            contador[posiciones.size()] += medio;
        }

        return contador[1] * 15 + contador[2] * 25 + contador[3] * 35 + contador[4] * 45 + contador[5] * 55 + contador[6] * 65 + contador[7] * 75 + contador[8] * 85 + contador[9] * 95 + contador[10] * 105 + contador[11] * 115;
    }

    public void Agrupadas(GameStatus gs, CellType color, int fila, int col) {
        int filAux = fila;
        int colAux = col;
        CellType colorRival = color.opposite(color); //color del contrario -*-=+
        Point PosicionActual = new Point(filAux, colAux);
        if (gs.getPos(filAux, colAux) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
            return;
        } else if (gs.getPos(filAux, colAux) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
            return;
        } else if (posiciones.contains(PosicionActual)) {
            return;
        } else {
            /*introducir fil, col a las posiciones*/
            posiciones.add(PosicionActual);
            for (int i = -1; i < 1; i++) {
                for (int j = -1; j < 1; j++) {
                    if (!(i == 0 && j == 0)) {
                        if (ComprobarPosicion(gs, filAux + i, colAux + j)) {
                            Agrupadas(gs, color, filAux + i, colAux + j);
                        }
                    }
                }
            }
        }
    }

    public int Enmedio(Point pos) {
        int X = pos.x;
        int Y = pos.y;
        int peso = 0;
        if (X >= 2 && X <= 5 && Y >= 2 && Y <= 5) {   //Zona ROJA - CENTRO
            peso = 10;
        } else if (X >= 1 && X <= 6 && Y >= 1 && Y <= 6 && !(X >= 2 && X <= 5 && Y >= 2 && Y <= 5)) { //Zona AZUL - MEDIO
            peso = 3;
        } else {        //ZONA VERDE - EXTERIOR
            peso = 1;
        }
        return peso;
    }

}
