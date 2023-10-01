import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;
    int last = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 2; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already sorted
     */
    public void distributeTilesToPlayers() {
        for(int i = 0 ; i<4; i++){
            for(int j = 0; j<14; j++){
                players[i].addTile(tiles[j]);
            }
        }
        players[0].addTile(tiles[56]);
        last++;
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        return lastDiscardedTile.toString();
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        int count = 0;
        for(int i = 0 ; i<players[currentPlayerIndex].numberOfTiles; i++){
            count++;
        }
        
        return players[currentPlayerIndex].getAndRemoveTile(count).toString();
    }

    /*
     * TODO:--DONE--should randomly shuffle the tiles array before game starts
     * Daib
     *      * Done!!! But take a look please

     */
    public void shuffleTiles(){
        ArrayList<Tile> copy = new ArrayList<>(Arrays.asList(tiles));
        Collections.shuffle(copy);
        //System.out.println(copy);
        
        for ( int i = 0; i < copy.size(); i++)
        {
            tiles[i] = copy.get(i);
        } 
        // System.out.println(Arrays.toString(tiles));

       
    }

    /*
     * TODO:--DONE--check if game still continues, should return true if current player
     * finished the game. Use calculateLongestChainPerTile method to get the
     * longest chains per tile.
     * To win, you need one of the following cases to be true:
     * - 8 tiles have length >= 4 and remaining six tiles have length >= 3 the last one can be of any length
     * - 5 tiles have length >= 5 and remaining nine tiles have length >= 3 the last one can be of any length
     * These are assuming we check for the win condition before discarding a tile
     * The given cases do not cover all the winning hands based on the original
     * game and for some rare cases it may be erroneous but it will be enough
     * for this simplified version
     * Daib
     *      * Done!!! But take a look please

     */
    public boolean didGameFinish() {
        int fourMore = 0, threeMore = 0, fiveMore = 0, i;
        boolean fstCond = threeMore >= 6 , scnCond = fiveMore >= 5, thrCond = threeMore >= 9; 

        Player currentPlayer = players[currentPlayerIndex];
            

        for ( i = 0; i < currentPlayer.getTiles().length; i++)
        {
            int[] longestArr = currentPlayer.calculateLongestChainPerTile();
            int longest = 0;

            for(int j = 0; j<longestArr.length; j++){
                if (longestArr[j] > longest) {
                    longest = longestArr[i]; 
                }
            }

            if ( longest >= 3)
            {
                threeMore++;
            }
            else if ( longest >= 4)
            {
                fourMore++;
            }
            else if ( longest >= 5)
            {
                fiveMore++;
            }
        }

        if ( fourMore >= 8)
        {
            return fstCond;
        }
        else
        {
            return ( scnCond && thrCond );
        }
        //return false;
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You may choose randomly or consider if the discarded tile is useful for
     * the current status. Print whether computer picks from tiles or discarded ones.
     * Isa
     * Done!!! But take a look please
     */
    public void pickTileForComputer() {
        Tile topTile = tiles[103];
        for(int i = last+1; i<tiles.length; i++ ){
            topTile = tiles[last];
        }
        if(currentPlayerIndex!=0){
            players[currentPlayerIndex].addTile(topTile);
            System.out.println("Picked a tile from the tiles stack");

        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * For this use the findLongestChainOf method in Player class to calculate
     * the longest chain length per tile of this player,
     * then choose the tile with the lowest chain length and discard it
     * this method should print what tile is discarded since it should be
     * known by other players
     * Isa
     */
    public void discardTileForComputer() {
        int playerNumber = currentPlayerIndex+1;
        ArrayList<Integer> arrayList = new ArrayList<>();
        if(currentPlayerIndex!=0){
            Player thisPlayer = players[currentPlayerIndex];
            for(int i = 0; i<thisPlayer.numberOfTiles;i++){
                arrayList.add(thisPlayer.findLongestChainOf(thisPlayer.getTiles()[i]));
            }
            int minimum = arrayList.get(0);
        for (int i = 1; i < arrayList.size(); i++) {
            if (minimum > arrayList.get(i))
                minimum = arrayList.get(i);
        }
       
        System.out.println("Tile "+ thisPlayer.getTiles()[minimum]+" has been removed for player "+ playerNumber);
                players[currentPlayerIndex].getAndRemoveTile(minimum);

        }
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     * Yusuf
     */
    public void discardTile(int tileIndex) {
        if(currentPlayerIndex==0){
            setLastDiscardedTile(tileIndex);
            players[currentPlayerIndex].getAndRemoveTile(tileIndex);
            getLastDiscardedTile();
        

        }
    }

    public void currentPlayerSortTilesColorFirst() {
        players[currentPlayerIndex].sortTilesColorFirst();
    }

    public void currentPlayerSortTilesValueFirst() {
        players[currentPlayerIndex].sortTilesValueFirst();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }
    public void setLastDiscardedTile(int i ){
        lastDiscardedTile = players[currentPlayerIndex].getTiles()[i];
    }

}
