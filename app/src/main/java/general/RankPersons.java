package general;


public class RankPersons {
    private String name, steps, caloryBurnt, weight;

    public RankPersons() {
    }

    public RankPersons(String name, String steps, String caloryBurnt, String weight) {
        this.name = name;
        this.steps = steps;
        this.caloryBurnt = caloryBurnt;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getCaloryBurnt() {
        return caloryBurnt;
    }

    public void setCaloryBurnt(String caloryBurnt) {
        this.caloryBurnt = caloryBurnt;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}

