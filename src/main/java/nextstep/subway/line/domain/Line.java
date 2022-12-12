package nextstep.subway.line.domain;

import nextstep.subway.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import nextstep.subway.station.domain.Stations;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        sections.add(new Section(this, upStation, downStation, distance));
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public Stations getStations() {
        if (sections.isEmpty()) {
            return new Stations();
        }
        Stations stations = new Stations();
        Station downStation = findFinalUpStation();
        while (downStation != null) {
            stations.add(downStation);
            downStation = nextStationOf(downStation, StationPosition.DOWN_STATION);
        }
        return stations;
    }

    public Station findFinalUpStation() {
        Station finalUpStation = null;
        Station nextUpstation = sections.get(0).getDownStation();
        while (nextUpstation != null) {
            finalUpStation = nextUpstation;
            nextUpstation = nextStationOf(finalUpStation, StationPosition.UP_STATION);
        }
        return finalUpStation;
    }

    public Station nextStationOf(Station station, StationPosition stationPosition) {
        Station downStation = null;
        Section nextSection = sections.stream()
                .filter(section -> section.isStationOppositeOf(station, stationPosition))
                .findFirst()
                .orElse(null);
        if (nextSection != null) {
            downStation = nextSection.getStationByPosition(stationPosition);
        }
        return downStation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
