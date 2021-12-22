/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.GameStatus;
import java.util.ArrayList;
import java.util.Random;

/**
 * Es com si estiguesim en la classe de GameStautus pero realment no hi estem,
 * és com un parasit
 *
 * @author Usuario
 */
public class MeuStatus extends GameStatus {

    int matrix[][][] = new int[8][8][2];
    
    GameStatus gs2 = new GameStatus(matrix);

    public MeuStatus(int[][] tauler) {
        super(tauler);
    }

    public MeuStatus(GameStatus gs) {
        super(gs);
    }

    //FER UN GET HAURISTICA ACÁ
    public int getHeuristica(GameStatus gs) {
        CellType color = gs.getCurrentPlayer();
        //int npiezas = gs.getNumberOfPiecesPerColor(color);
        NotCheckers NC = new NotCheckers("NotCheckers", 4);
        int h = NC.heuristica(gs, color);
        return h;
    }

    public void setValorRandomCasillas(GameStatus gs, CellType color) {

        CellType ColorContrario = color.opposite(color);
        
        Random rand = new Random();
        int randomBlancas = rand.nextInt(10000);
        int randomNegras = rand.nextInt(10000);
        
        int numColor = color.toColor01(color);
        int numColorContrario = ColorContrario.toColor01(ColorContrario);
        
        for (int i; i < gs.getSize(); i++) {
            for (int j; j < gs.getSize(); j++) {
                if (numColor == 0) {
                    gs[i]j] = randomBlancas;
                }
            }
        }
    }
}
