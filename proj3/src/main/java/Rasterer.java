import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private static final double ROOT_LONDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
    private static final double ROOT_LATDPP = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / MapServer.TILE_SIZE;

    public Rasterer() {}

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    private double LonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }

    private double LatDPP(double lrlat, double ullat, double height) {
        return (ullat - lrlat) / height;
    }

    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        // First, we should get the query box and the user viewport width and height.
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");

        // Next, we should compute the LonDPP and LatDPP of the query box.
        double LonDPP = LonDPP(lrlon, ullon, width);
        double LatDPP = LatDPP(lrlat, ullat, height);

        // Then, we should find the depth of the grid that best matches the query box.
        int depth = 0;
        while ((LonDPP < ROOT_LONDPP / Math.pow(2, depth) || LatDPP < ROOT_LATDPP / Math.pow(2, depth)) && depth < 7) {
            depth++;
        }

        // Now, we should find the grid of images that best matches the query.
        int xStart = 0, xEnd = 0, yStart = 0, yEnd = 0;
        double lonDPP = ROOT_LONDPP / Math.pow(2, depth), latDPP = ROOT_LATDPP / Math.pow(2, depth);
        int lonStart = (int) Math.round(Math.floor((ullon - MapServer.ROOT_ULLON) / (lonDPP * MapServer.TILE_SIZE)));
        int lonEnd = (int) Math.round(Math.floor((lrlon - MapServer.ROOT_ULLON) / (lonDPP * MapServer.TILE_SIZE)));
        int latStart = (int) Math.round(Math.floor((MapServer.ROOT_ULLAT - ullat) / (latDPP * MapServer.TILE_SIZE)));
        int latEnd = (int) Math.round(Math.floor((MapServer.ROOT_ULLAT - lrlat) / (latDPP * MapServer.TILE_SIZE)));

        xStart = Math.max(lonStart, 0);
        xEnd = lonEnd > Math.pow(2, depth) - 1 ? (int) Math.pow(2, depth) - 1 : lonEnd;
        yStart = Math.max(latStart, 0);
        yEnd = latEnd > Math.pow(2, depth) - 1 ? (int) Math.pow(2, depth) - 1 : latEnd;

        // Finally, we should return the results for the front end.
        Map<String, Object> results = new HashMap<>();

        // 1. "render_grid" : String[][], the files to display.
        String render_grid[][] = new String[yEnd - yStart + 1][xEnd - xStart + 1];
        for (int i = yStart; i <= yEnd; i++) {
            for (int j = xStart; j <= xEnd; j++) {
                render_grid[i - yStart][j - xStart] = "d" + depth + "_x" + j + "_y" + i + ".png";
            }
        }
        results.put("render_grid", render_grid);

        // 2. "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image.
        double raster_ul_lon = MapServer.ROOT_ULLON + xStart * lonDPP * MapServer.TILE_SIZE;
        results.put("raster_ul_lon", raster_ul_lon);

        // 3. "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image.
        double raster_ul_lat = MapServer.ROOT_ULLAT - yStart * latDPP * MapServer.TILE_SIZE;
        results.put("raster_ul_lat", raster_ul_lat);

        // 4. "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image.
        double raster_lr_lon = MapServer.ROOT_ULLON + (xEnd + 1) * lonDPP * MapServer.TILE_SIZE;
        results.put("raster_lr_lon", raster_lr_lon);

        // 5. "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image.
        double raster_lr_lat = MapServer.ROOT_ULLAT - (yEnd + 1) * latDPP * MapServer.TILE_SIZE;
        results.put("raster_lr_lat", raster_lr_lat);

        // 6. "depth" : Number, the depth of the nodes of the rastered image
        results.put("depth", depth);

        // 7. "query_success" : Boolean, whether the query was able to successfully complete; don't forget to set this to true on success!
        results.put("query_success", true);

        return results;
    }

}
