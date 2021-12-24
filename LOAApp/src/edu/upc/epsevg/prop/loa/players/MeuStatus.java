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
    //int hashKey = 0;

    /**
     * Funció crida de l'heurística feta a la classe NotCheckers
     *
     *
     * @param gs Tauler on es farà el millor moviment donat
     *
     * @return h - Valor de l'heurística trobada al NotCheckers
     */
    /*
    public int getHeuristica(GameStatus gs) {
        //CellType color = gs.getCurrentPlayer();
        //int npiezas = gs.getNumberOfPiecesPerColor(color);
        NotCheckers NC = new NotCheckers("NotCheckers", 4);
        int h = NC.heuristica(gs, color);
        return h;
    }
*/
    //Generador random pot generar tamb negatius
    public static int randomValue() {
        SecureRandom random = new SecureRandom();
        return random.nextInt();
    }

    //Inicializador de taula
    public void setValorRandomCasillas() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int piece = 0; piece < 2; piece++) {
                    ZobristTable[i][j][piece] = randomValue();
                    //System.out.println("i: " + i + "j: " + j + "valores:" + ZobristTable[i][j][piece]);
                }
            }
        }
    }

    //Calcula el valor hash d'un tauler donat (fa la XOR)
    public int HashValues() {
        int hashValue = 0; //[X][Y][color]
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                CellType pos = this.getPos(i, j);
                if (pos != CellType.EMPTY) {
                    //CellType piece = ms.getCurrentPlayer();
                    hashValue ^= ZobristTable[i][j][color.toColor01(color)];
                }
            }
        }
        return hashValue;
    }

    /*
    public int actualizarHash(GameStatus ms) {
        NotCheckers nc = new NotCheckers("NotCheckers", 4);
        Move moviment = nc.move(ms);
        Point PosFrom = moviment.getFrom();
        Point PosTo = moviment.getTo();
        CellType EstatCasilla;
        CellType color = ms.getCurrentPlayer();
        EstatCasilla = ms.getPos(PosTo);
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
            super.movePiece(PosFrom, PosTo);
        }
        return hashKey;
    }
     */
    public void actualizarHash(Point PosFrom, Point PosTo) {
        //System.out.println("HASHKEY: "+hashKey);
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
            //super.movePiece(PosFrom, PosTo);
            System.out.println("hemos llegado a actualizar "+hashKey);
        }
    }

    @Override
    public void movePiece(Point PosFrom, Point PosTo) {
        actualizarHash(PosFrom,PosTo);
        super.movePiece(PosFrom, PosTo); //To change body of generated methods, choose Tools | Templates.
    }
}
