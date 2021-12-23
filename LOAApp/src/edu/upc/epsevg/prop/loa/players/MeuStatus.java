/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.GameStatus;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Es com si estiguesim en la classe de GameStautus pero realment no hi estem,
 * és com un parasit
 *
 * @author Omara Aibar i Alexandra Espinosa
 */
public class MeuStatus extends GameStatus { 

    public MeuStatus(int[][] tauler) {
        super(tauler);
    }

    public MeuStatus(GameStatus gs) {
        super(gs);
    }
    //Globals
    static long ZobristTable[][][] = new long [8][8][2]; //[X][Y][color]
    
    
    
    /**
     * Funció crida de l'heurística feta a la classe NotCheckers
     * 
     *
     * @param gs Tauler on es farà el millor moviment donat
     * 
     * @return h - Valor de l'heurística trobada al NotCheckers
     */
    public int getHeuristica(GameStatus gs) {
        CellType color = gs.getCurrentPlayer();
        //int npiezas = gs.getNumberOfPiecesPerColor(color);
        NotCheckers NC = new NotCheckers("NotCheckers", 4);
        int h = NC.heuristica(gs, color);
        return h;
    }
/*
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
 */ 
    //Generador random pot generar tamb negatius
    public static long randomValue(){
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }

    //Inicializador de taula
    public void setValorRandomCasillas(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                for(int piece = 0; piece < 2; piece++){
                    ZobristTable[i][j][piece] = randomValue();
                    System.out.println("i: "+i + "j: "+j + "valores:" +ZobristTable[i][j][piece]);
                }
            }
        }
    }

    //Calcula el valor hash d'un tauler donat (ho de XOR)
    public long calculHash(MeuStatus ms){
        long hashKey = 0; //[X][Y][color]
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                CellType pos = ms.getPos(i, j);
                if(pos!= CellType.EMPTY){
                    CellType piece = ms.getCurrentPlayer();
                    hashKey ^= ZobristTable[i][j][piece.toColor01(piece)];
                } 
            }
        }
        return hashKey;
    }
    
    /*public long actualitza(MeuStatus ms,NotCheckers nc){
        long act = 0;
        
    }*/
    int main(){
        setValorRandomCasillas();
        
        return 0;
    }






}

