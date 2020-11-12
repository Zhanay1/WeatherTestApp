package com.example.weather;

import java.util.ArrayList;
import java.util.List;

public class City {
    private List<String> cities;

    public City() {
        cities = new ArrayList<>();
        addCities();
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public void addCities() {
        cities.add("Oral");
        cities.add("Oktyabr'sk");
        cities.add("Qulsary");
        cities.add("Khromtau");
        cities.add("Karagandy");
        cities.add("Emba");
        cities.add("Balyqshy");
        cities.add("Atyrau");
        cities.add("Aqtobe");
        cities.add("Aktau");
        cities.add("Aqsay");
        cities.add("Zyryanovsk");
        cities.add("Zhosaly");
        cities.add("Zhezqazghan");
        cities.add("Zhetiqara");
        cities.add("Zhangatas");
        cities.add("Ayteke Bi");
        cities.add("Taraz");
        cities.add("Zaysan");
        cities.add("Zhangaqorghan");
        cities.add("Turar Ryskulov");
        cities.add("Ush-Tyube");
        cities.add("Turkestan");
        cities.add("Temirtau");
        cities.add("Tekeli");
        cities.add("Tasboget");
        cities.add("Talghar");
        cities.add("Taldyqorghan");
        cities.add("Shymkent");
        cities.add("Shu");
        cities.add("Shemonaikha");
        cities.add("Shchuchinsk");
        cities.add("Semey");
        cities.add("Saryaghash");
        cities.add("Sarkand");
        cities.add("Sorang");
        cities.add("Rudnyy");
        cities.add("Qyzylorda");
        cities.add("Qostanay");
        cities.add("Karatau");
        cities.add("Qapshaghay");
        cities.add("Petropavlovsk");
        cities.add("Pavlodar");
        cities.add("Zharkent");
        cities.add("Ust-Kamenogorsk");
        cities.add("Sarykemer");
        cities.add("Merke");
        cities.add("Makinsk");
        cities.add("Lisakovsk");
        cities.add("Baykonyr");
        cities.add("Ridder");
        cities.add("Lenger");
        cities.add("Kokshetau");
        cities.add("Kentau");
        cities.add("Esik");
        cities.add("Georgievka");
        cities.add("Aqsu");
        cities.add("Energeticheskiy");
        cities.add("Ekibastuz");
        cities.add("Shieli");
        cities.add("Shardara");
        cities.add("Burunday");
        cities.add("Aksu");
        cities.add("Balqash");
        cities.add("Ayagoz");
        cities.add("Atbasar");
        cities.add("Arys");
        cities.add("Arkalyk");
        cities.add("Aral");
        cities.add("Astana");
        cities.add("Almaty");
        cities.add("Akkol");
        cities.add("Abay");
        cities.add("Stepnogorsk");
    }

    public List<String> searchCities(String q) {
        List<String> result = new ArrayList<>();
        for(String s: this.getCities()) {
            if (s.toLowerCase().startsWith(q.toLowerCase())){
                result.add(s);
            }
        }

        return result;
    }
}