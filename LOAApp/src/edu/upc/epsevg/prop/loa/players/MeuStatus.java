/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.GameStatus;

/**
 * Es com si estiguesim en la classe de GameStautus pero realment no hi estem,
 * és com un parasit
 *
 * @author Usuario
 */
public class MeuStatus extends GameStatus {

    public MeuStatus(int[][] tauler) {
        super(tauler);
    }

    public MeuStatus(GameStatus gs) {
        super(gs);
    }

    //FER UN GET HAURISTICA ACÁ
    public int getHeuristica(GameStatus gs) {
        CellType color = gs.getCurrentPlayer();
        int npiezas = gs.getNumberOfPiecesPerColor(color);
        return npiezas;
    }

}
