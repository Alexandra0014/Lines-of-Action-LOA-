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
    int prof = 0;
    int infneg = -99999999;    //-inf
    int infpos = 99999999;     // +inf
    int cont = 0;             //contador dels nodes explorats (OJO HAY UNA FUNCIÓN EN Move que te lo dice)
    CellType jugadorACT;
    Point PosFrom;
    Point PosTo;

//-------------------------------
    public NotCheckers(String name, int p) {
        this.name = name;
        prof = p;
    }

    @Override
    public Move move(GameStatus gs) {
        int valor = infneg;
        int alfa = infneg;
        int beta = infpos;
        int millorMov = 0;  //Millor pos. on es possarà les fitxes
        int heu = 0;        //Resultat de l'heuristica
        CellType color = gs.getCurrentPlayer();

        for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
            PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
            ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
            if (points.size() != 0) { //si nos podemos mover
                for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                    PosTo = points.get(mov); //Obtenemos una posicion de destino
                    GameStatus aux = new GameStatus(gs);
                    aux.movePiece(PosFrom, PosTo);
                    CellType colorRival = color.opposite(color);
                    heu = min_Valor(aux, colorRival, alfa, beta, prof - 1, mov);
                    if (valor <= heu) {
                        millorMov = i;
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
                        GameStatus aux = new GameStatus(gs);
                        aux.movePiece(PosFrom, PosTo);
                        CellType colorRival = color.opposite(color); //color del contrario -*-=+
                        valor = Integer.min(valor, max_Valor(aux, colorRival, alfa, beta, prof - 1, mov));
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
        if (gs.isGameOver()) {
            return infneg;

        } else if (profunditat == 0) {
            //return heuristica(t, color * -1);
            return 0;

        } else {
            valor = infneg;
            for (int i = 0; i < gs.getNumberOfPiecesPerColor(color); i++) { //recorremos las piezas del tablero
                PosFrom = gs.getPiece(color, i); //obtenemos la posicion de cada pieza
                ArrayList<Point> points = gs.getMoves(PosFrom); //lista de posiciones a las que podemos movernos
                if (points.size() != 0) { //si nos podemos mover
                    for (int mov = 0; mov < points.size(); mov++) { //recorremos la lista de movimientos posibles por cada pieza
                        PosTo = points.get(mov); //Obtenemos una posicion de destino
                        GameStatus aux = new GameStatus(gs);
                        aux.movePiece(PosFrom, PosTo);
                        CellType colorRival = color.opposite(color); //color del contrario -*-=+
                        valor = Integer.max(valor, min_Valor(aux, colorRival, alfa, beta, prof - 1, mov));
                        alfa = Integer.min(valor, beta);
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
        public void timeout
                
        
            () {
        TimeOut = true;
        }

        @Override
        public String getName
                
        
            () {
        return name;
        }

    }
