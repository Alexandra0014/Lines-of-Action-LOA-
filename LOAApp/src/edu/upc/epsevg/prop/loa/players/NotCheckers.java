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
    int prof = 8;
    int infneg = -99999999;    //-inf
    int infpos = 99999999;     // +inf
    int cont = 0;             //contador dels nodes explorats (OJO HAY UNA FUNCIÓN EN Move que te lo dice)

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
        int millorMov = 0;  //Millor pos. on es possarà les fitxes
        int heu = 0;        //Resultat de l'heuristica
        CellType color = gs.getCurrentPlayer();
        int profunditat = prof;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        Move movi = new Move(PosFrom,PosTo,0,0,SearchType.MINIMAX);

        for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
            PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
            ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
            if (!points.isEmpty()) { //si nos podemos mover
                for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                    PosTo = points.get(mov); //Obtenemos una posicion de destino
                    GameStatus aux = new GameStatus(gs);
                    aux.movePiece(PosFrom, PosTo);
                    CellType colorRival = color.opposite(color);
                    heu = min_Valor(aux, colorRival, alfa, beta, profunditat - 1, mov);
                    if (valor <= heu) {
                        millorMov = mov;
                        valor = heu;
                        movi = new Move(PosFrom, PosTo, 0, 0, SearchType.MINIMAX);
                    }
                }
            }
        }
        return movi;
    }

    public int min_Valor(GameStatus gs, CellType color, int alfa, int beta, int profunditat, int col) {
        int valor;
        cont++;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        if (gs.isGameOver()) {
            return infpos;

        } else if (profunditat == 0) {
            return Nagrupadas(gs,color);
            //return heuristica(gs, color);

        } else {
            valor = infpos;
            for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if (points.size() != 0) { //si nos podemos mover
                    for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux2 = new GameStatus(gs);
                        aux2.movePiece(PosFrom, PosTo);
                        CellType colorRival = color.opposite(color); //color del contrario -*-=+
                        valor = Integer.min(valor, max_Valor(aux2, colorRival, alfa, beta, profunditat - 1, mov));
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

    public int max_Valor(GameStatus gs, CellType color, int alfa, int beta, int profunditat, int col) {
        int valor;
        cont++;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        if (gs.isGameOver()) {
            return infneg;

        } else if (profunditat == 0) {
            return Nagrupadas(gs,color);
            //return heuristica(gs, color);

        } else {
            valor = infneg;
            for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if (points.size() != 0) { //si nos podemos mover
                    for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux2 = new GameStatus(gs);
                        aux2.movePiece(PosFrom, PosTo);
                        CellType colorRival = color.opposite(color); //color del contrario -*-=+
                        valor = Integer.max(valor, min_Valor(aux2, colorRival, alfa, beta, profunditat - 1, mov));
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
    public int heuristica (GameStatus gs, CellType color) {
        int rival = 0;
        int nuestras = 0;
        CellType colorRival = color.opposite(color); 
        rival = Nagrupadas(gs,colorRival);
        nuestras = Nagrupadas(gs,color);
        
        return nuestras - rival;
    }
    
    public int Nagrupadas(GameStatus gs, CellType color) {   //numero de fitxes afrupades al tauler
        int nºagrupades = 0;
        int FichasTablero = gs.getNumberOfPiecesPerColor(color);
        int contador[] = new int[11];
        for (int i = 0; i < gs.getSize(); i++) {
            for (int j = 0; j < gs.getSize(); j++) {
                if (gs.getPos(i,j) == color) {
                   nºagrupades = Agrupadas(gs, color, i, j);
                   
                   if (nºagrupades>0 && nºagrupades<FichasTablero) {
                       contador[nºagrupades-1]++;
                   }
                }
                
            }
        }
        return contador[0] * 5 + contador[1] * 15 + contador[2] * 25 + contador[3] * 35 + contador[4] * 45 + contador[5] * 55 + contador[6] * 65 + contador[7] * 75 + contador[8] * 85 + contador[9] * 95 + contador[10] * 105;
        //return nºagrupades;
    }

    public int Agrupadas(GameStatus gs, CellType color, int fila, int col) {
        int direccion = 1; //indica en la direccion que estamos explorando
        int contFichas = 1; //cuenta la cantidad de ficha agrupadas que tenemos
        int FichasTablero = gs.getNumberOfPiecesPerColor(color); //como inicia la fichas que hay en el tablero
        int filAux = fila;
        int colAux = col;
        CellType colorRival = color.opposite(color); //color del contrario -*-=+

        while (direccion < 8 && contFichas < FichasTablero) { //mientras no hayamos hecho la vuelta del reloj y no hayamos llegado al total de fichas que tenemos de un solo color en el tablero

            if (direccion == 1) { //exploramos en vertical [i+1,j]
                if (ComprobarPosicion(gs, filAux + 1, colAux)) {
                    if (gs.getPos(filAux + 1, colAux) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux++;
                    } else if (gs.getPos(filAux + 1, colAux) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux + 1, colAux) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }
            }
            if (direccion == 2) { //exploramos la diagonal derecha arriba [i+1,j+1]
                filAux = fila;
                colAux = col;
                if (ComprobarPosicion(gs, filAux + 1, colAux + 1)) {
                    if (gs.getPos(filAux + 1, colAux + 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux++;
                        colAux++;
                    } else if (gs.getPos(filAux + 1, colAux + 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux + 1, colAux + 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 3) { //exploramos la horizontal derecha [i,j+1]
                colAux = col;
                filAux = fila;
                if (ComprobarPosicion(gs, filAux, colAux + 1)) {
                    if (gs.getPos(filAux, colAux + 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        colAux++;
                    } else if (gs.getPos(filAux, colAux + 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux, colAux + 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 4) { //exploramos la diagonal derecha abajo [i-1,j+1]
                colAux = col;
                filAux = fila;
                if (ComprobarPosicion(gs, filAux - 1, colAux + 1)) {
                    if (gs.getPos(filAux - 1, colAux + 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux--;
                        colAux++;
                    } else if (gs.getPos(filAux - 1, colAux + 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux - 1, colAux + 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 5) { //exploramos la vertical abajo [i-1,j]
                colAux = col;
                filAux = fila;
                if (ComprobarPosicion(gs, filAux - 1, colAux)) {
                    if (gs.getPos(filAux - 1, colAux) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux--;
                    } else if (gs.getPos(filAux - 1, colAux) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux - 1, colAux) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 6) { //exploramos la diagonal izquierda abajo [i-1,j-1]
                colAux = col;
                filAux = fila;
                if (ComprobarPosicion(gs, filAux - 1, colAux - 1)) {
                    if (gs.getPos(filAux - 1, colAux - 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux--;
                        colAux--;
                    } else if (gs.getPos(filAux - 1, colAux - 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux - 1, colAux - 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 7) { //exploramos la horizontal izquierda [i,j-1]
                colAux = col;
                filAux = fila;
                if (ComprobarPosicion(gs, filAux, colAux - 1)) {
                    if (gs.getPos(filAux, colAux - 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        colAux--;
                    } else if (gs.getPos(filAux, colAux - 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux, colAux - 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }
            if (direccion == 8) { //exploramos la diagonal izquierda arriba [i+1,j-1]
                colAux = col;
                filAux = fila; 
                if (ComprobarPosicion(gs, filAux + 1, colAux - 1)) {
                    if (gs.getPos(filAux + 1, colAux - 1) == color) { //si nos encontramos una ficha de nuestro color
                        contFichas++;
                        filAux++;
                        colAux--;
                    } else if (gs.getPos(filAux + 1, colAux - 1) == colorRival) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                        direccion++;
                    } else if (gs.getPos(filAux + 1, colAux - 1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                        direccion++;
                    }
                } else {
                    direccion++;
                }

            }

        }
        //System.out.println("contFichas: " + contFichas);
        return contFichas;
    }

}
