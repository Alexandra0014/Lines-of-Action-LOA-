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

        for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
            PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
            ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
            if (points.size() != 0) { //si nos podemos mover
                for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                    PosTo = points.get(mov); //Obtenemos una posicion de destino
                    GameStatus aux = new GameStatus(gs);
                    aux.movePiece(PosFrom, PosTo);
                    CellType colorRival = color.opposite(color);
                    heu = min_Valor(aux, colorRival, alfa, beta, profunditat - 1, mov);
                    if (valor <= heu) {
                        millorMov = mov;
                        valor = heu;
                    }
                }
            }
        }
        return new Move(PosFrom, PosTo, 0, 0, SearchType.MINIMAX);
    }

    public int min_Valor(GameStatus gs, CellType color, int alfa, int beta, int profunditat, int col) {
        int valor;
        cont++;
        Point PosFrom = new Point(0, 0);
        Point PosTo = new Point(0, 0);
        if (gs.isGameOver()) {
            return infpos;

        } else if (profunditat == 0) {
            //return heuristica(t, color * -1);
            return 0;

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
            //return heuristica(t, color);
            return 0;

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
    
    public int Agrupadas (GameStatus gs, CellType color, int fila, int col) {
        int direccion = 1; //indica en la direccion que estamos explorando
        int contFichas = 1; //cuenta la cantidad de ficha agrupadas que tenemos
        int FichasTablero = gs.getNumberOfPiecesPerColor(color); //como inicia la fichas que hay en el tablero
        int filAux = fila;
        int colAux = col;
        CellType colorRival = color.opposite(color); //color del contrario -*-=+
        
        while(direccion<8 && contFichas<FichasTablero) { //mientras no hayamos hecho la vuelta del reloj y no hayamos llegado al total de fichas que tenemos de un solo color en el tablero
            gs.validateCordinates(filAux, colAux); // validar si la posicion es correcta
            if (direccion == 1) { //exploramos en vertical [i+1,j]
                 gs.validateCordinates(filAux+1, colAux);
                 if (gs.getPos(filAux+1,colAux) == color) { //si nos encontramos una ficha de nuestro color
                    contFichas++;
                    filAux++;
                 }
                 else if (gs.getPos(filAux+1,colAux) == colorRival ) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                     direccion++; 
                 }
                 else if (gs.getPos(filAux+1,colAux) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                     direccion++;
                 }
            }
            if (direccion == 2) { //exploramos la diagonal derecha arriba [i+1,j+1]
                gs.validateCordinates(filAux+1, colAux+1);
                if (gs.getPos(filAux+1,colAux+1) == color) { //si nos encontramos una ficha de nuestro color
                    contFichas++;
                    filAux++;
                    colAux++;
                 }
                 else if (gs.getPos(filAux+1,colAux+1) == colorRival ) { //si encontramos la ficha del rival cambiamos la direccion de exploracion
                     direccion++; 
                 }
                 else if (gs.getPos(filAux+1,colAux+1) == CellType.EMPTY) { //si encontramos una casilla blanca cambiamos la direccion de exploracion
                     direccion++;
                 }
            }
        }
        
        return contFichas;
    }

}
