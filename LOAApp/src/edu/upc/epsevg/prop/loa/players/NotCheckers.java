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
    int infneg= -99999999;    //-inf
    int infpos= 99999999;     // +inf
    int cont = 0;             //contador dels nodes explorats (OJO HAY UNA FUNCIÓN EN Move que te lo dice)
    CellType jugadorACT;
    
//-------------------------------
    public NotCheckers(String name,int p) {
        this.name = name;
        prof = p;
    }

    public int Tria_Moviment(GameStatus bstatus, CellType player, int profunditat, Point pos){
        int alfa = infneg;
        int beta = infpos;
        int millorMov = 0;  //Millor pos. on es possarà les fitxes
        int heu = 0;        //Resultat de l'heuristica
        Point posFrom = bstatus.
        for(int col = 0; col < bstatus.getSize(); col++)  //Recorrem totes les columnes de la taula sense passar-nos del size
        {
            ArrayList<Point> points =  bstatus.getMoves(pos);
            if (points.size() != 0) {
                GameStatus prova = new GameStatus(bstatus);
                prova.movePiece(pos, pos);
            }
            
            
        }
         
    }

    @Override
    public Move move(GameStatus gs) {
        
        CellType color = gs.getCurrentPlayer();
        this.gs = gs;
        int qn = gs.getNumberOfPiecesPerColor(color);
        ArrayList<Point> pendingAmazons = new ArrayList<>();
        for (int q = 0; q < qn; q++) {
            pendingAmazons.add(gs.getPiece(color, q));
        }
        // Iterem aleatòriament per les peces fins que trobem una que es pot moure.
        Point queenTo = null;
        Point queenFrom = null;
        while (queenTo == null) {
            Random rand = new Random();
            int q = rand.nextInt(pendingAmazons.size());
            queenFrom = pendingAmazons.remove(q);
            //queenTo = posicioRandomAmazon(gs, queenFrom);
        }

        // "s" és una còpia del tauler, per es pot manipular sense perill
        gs.movePiece(queenFrom, queenTo);

        return new Move(queenFrom, queenTo, 0, 0, SearchType.RANDOM);
    }

    @Override
    public void timeout() {
        TimeOut = true;
    }

    @Override
    public String getName() {
        return name;
    }
    

}
