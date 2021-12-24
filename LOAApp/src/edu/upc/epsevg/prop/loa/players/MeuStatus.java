/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.GameStatus;
import edu.upc.epsevg.prop.loa.Move;
import java.awt.Point;
import static java.nio.file.Files.move;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Es com si estiguesim en la classe de GameStautus pero realment no hi estem,
 * és com un parasit, es a dir, es una extensió de la classe GameStatus.
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
    static int ZobristTable[][][] = new int[8][8][2]; //[X][Y][color]
    CellType color = this.getCurrentPlayer();

    /**
     * Funció crida de l'heurística feta a la classe NotCheckers
     *
     * @param gs Tauler on es farà el millor moviment donat
     *
     * @return h - Valor de l'heurística trobada al NotCheckers
     */
    
    public int getHeuristica(GameStatus gs) {
        NotCheckers NC = new NotCheckers("NotCheckers", 4);
        int h = NC.heuristica(gs, color);
        return h;
    }

    /**
     * S'encarrega de generar un numero aleatori
     * @return Retorna un numero aleatori
     */
    
    //Generador random pot generar tamb negatius
    public static int randomValue() {
        SecureRandom random = new SecureRandom();
        return random.nextInt();
    }
    /**
     * S'encarrega d'assignar dos valors aleatoris per a cada posició de la matriu
     */
    //Inicializador de taula
    public void setValorRandomCasillas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int piece = 0; piece < 2; piece++) {
                    ZobristTable[i][j][piece] = randomValue();
                }
            }
        }
    }

    /**
     * Fa el calcul de la XOR per a cada posicio sempre que la posicio no estigui buida
     * @return hashValue Retorna el valor de la hash
     */
    
    //Calcula el valor hash d'un tauler donat (fa la XOR)
    public int HashValues() {
        int hashValue = 0; //[X][Y][color]
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellType pos = this.getPos(i, j);
                if (pos != CellType.EMPTY) {
                    hashValue ^= ZobristTable[i][j][color.toColor01(color)];
                }
            }
        }
        return hashValue;
    }

   /**
    * S'encarrega d'actualitzar el valor de la hash fent els diferents calculs de les XOR
    * @param PosFrom Indica la posicio origen
    * @param PosTo Indica la posicio destí
    */
    public void actualizarHash(Point PosFrom, Point PosTo) {
        int hashKey = HashValues();
        setValorRandomCasillas();
        CellType EstatCasilla;
        EstatCasilla = this.getPos(PosTo);
        CellType ColorRival = color.opposite(color);
        if (EstatCasilla != CellType.EMPTY) {
            if (EstatCasilla == ColorRival) {
                hashKey ^= ZobristTable[PosFrom.x][PosFrom.y][color.toColor01(color)];        //Posició origen 
                hashKey ^= ZobristTable[PosTo.x][PosTo.y][ColorRival.toColor01(ColorRival)];  //Posició destí amb el color contrari
                hashKey ^= ZobristTable[PosTo.x][PosTo.y][color.toColor01(color)];            //Posició destí amb el teu color 
            }
            if (EstatCasilla == color) {
                hashKey ^= ZobristTable[PosFrom.x][PosFrom.y][color.toColor01(color)];    //Posició origen 
                hashKey ^= ZobristTable[PosTo.x][PosTo.y][color.toColor01(color)];        //Posició destí amb el teu color
            }
         
            System.out.println("hemos llegado a actualizar "+hashKey);
        }
    }
/*
    @Override
    public void movePiece(Point PosFrom, Point PosTo) {
        actualizarHash(PosFrom,PosTo);
        super.movePiece(PosFrom, PosTo);
    }
*/
}
