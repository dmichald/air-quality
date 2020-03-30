package com.example.smog;

public class Measurement {
    private String city;
    private String date;
    private String aqi;
    private String pm25;
    private String pm10;
    private String o3;
    private String no2;
    private String so2;
    private String co;
    private String temperature;
    private String status;

    private Measurement() {
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }

    public String getAqi() {
        return aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public String getO3() {
        return o3;
    }

    public String getNo2() {
        return no2;
    }

    public String getSo2() {
        return so2;
    }

    public String getCo() {
        return co;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getStatus() {
        return status;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private String status;
        private String city;
        private String date;
        private String aqi;
        private String pm25;
        private String pm10;
        private String o3;
        private String no2;
        private String so2;
        private String co;
        private String temperature;


        private Builder() {
        }

        Builder status(String status) {
            this.status = status;
            return this;
        }

        Builder city(String city) {
            this.city = city;
            return this;
        }

        Builder date(String date) {
            this.date = date;
            return this;
        }

        Builder aqi(String aqi) {
            this.aqi = aqi;
            return this;
        }

        Builder pm25(String pm25) {
            this.pm25 = pm25;
            return this;
        }

        Builder pm10(String pm10) {
            this.pm10 = pm10;
            return this;
        }

        Builder o3(String o3) {
            this.o3 = o3;
            return this;
        }

        Builder no2(String no2) {
            this.no2 = no2;
            return this;
        }

        Builder so2(String so2) {
            this.so2 = so2;
            return this;
        }

        Builder co(String co) {
            this.co = co;
            return this;
        }

        Builder temperature(String temperature) {
            this.temperature = temperature;
            return this;
        }


        public Measurement build() {
            Measurement measurement = new Measurement();
            measurement.city = this.city;
            measurement.date = this.date;
            measurement.aqi = this.aqi;
            measurement.pm25 = this.pm25;
            measurement.pm10 = this.pm10;
            measurement.o3 = this.o3;
            measurement.no2 = this.no2;
            measurement.so2 = this.so2;
            measurement.co = this.co;
            measurement.temperature = this.temperature;
            measurement.status = this.status;
            return measurement;
        }
    }


}
