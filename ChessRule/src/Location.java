/**
 * Name: Location
 * Usage:
 *   package two Integer x and y into one object
 *   Immutable
 *
 * Created by Xuhao Chen on 2016/12/29.
 */
public class Location {
    public final Integer x;
    public final Integer y;
    public Location(Integer x, Integer y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Location location){
        return (location.x == this.x) && (location.y == this.y) ;
    }
}
