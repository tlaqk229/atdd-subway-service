package nextstep.subway.path.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations = new ArrayList<>();

    private int pathDistance;

    private double fare;

    public static PathResponse from(Stations pathStations) {
        return new PathResponse(pathStations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()));
    }

    public PathResponse() {
    }

    private PathResponse(List<StationResponse> stations) {
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getPathDistance() {
        return pathDistance;
    }

    public double getFare() {
        return fare;
    }
}
