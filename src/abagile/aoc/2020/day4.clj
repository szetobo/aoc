(ns abagile.aoc.2020.day4
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]
    [malli.core :as m]
    [malli.error :as me]))

(defn input []
  (->> (cs/split (slurp (io/resource "day4.txt")) #"\n\n")
       (map #(re-seq #"(byr|iyr|eyr|hgt|hcl|ecl|pid|cid):([^\s]+)" %))
       (map #(into (hash-map) (map (comp vec rest) %)))))

(def read-int (fnil read-string "0"))

(defn valid? [part]
  (fn [m]
    (let [{:strs [byr iyr eyr hgt hcl ecl pid]} m]
      (and
        (every? some? [byr iyr eyr hgt hcl ecl pid])
        (if (= part 1) true
          (and
            (let [byr (re-matches #"\d+" byr)] (<= 1920 (read-int byr) 2002))
            (let [iyr (re-matches #"\d+" iyr)] (<= 2010 (read-int iyr) 2020))
            (let [eyr (re-matches #"\d+" eyr)] (<= 2020 (read-int eyr) 2030))
            (let [[_ cm in] (re-matches #"(\d{3})cm|(\d{2})in" hgt)]
              (or (<= 150 (read-int cm) 193)
                  (<= 59  (read-int in) 76)))
            (re-matches #"#[0-9|a-f]{6}" hcl)
            (re-matches #"amb|blu|brn|gry|grn|hzl|oth" ecl)
            (re-matches #"\d{9}" pid)))))))

(defn -main [& _]
  (println "part 1:" (->> (input)
                          (filter (valid? 1))
                          count))

  (println "part 2:" (->> (input)
                          ; (take 10)
                          (filter (valid? 2))
                          count)))

(def schema
  [:map
   ["byr" [:fn {:error/message "must be between 1920 and 2002"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 1920 (read-int v) 2002))]]
   ["iyr" [:fn {:error/message "must be between 2010 and 2020"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 2010 (read-int v) 2020))]]
   ["eyr" [:fn {:error/message "must be between 2020 and 2030"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 2020 (read-int v) 2030))]]
   ["hgt" [:fn {:error/message "must be between 150cm and 193cm, or 59in and 76in"}
           #(let [[_ v unit] (re-matches #"(\d+)(cm|in)" %)]
              (if (= "cm" unit)
                (<= 150 (read-int v) 193)
                (<= 59  (read-int v) 76)))]]
   ["hcl" [:re {:error/message "must be a valid hex color string, e.g #c0c0c0"}
           #"^#[0-9|a-f]{6}$"]]
   ["ecl" [:enum {:error/message "must be amb|blu|brn|gry|grn|hzl|oth"}
           "amb" "blu" "brn" "gry" "grn" "hzl" "oth"]]
   ["pid" [:re {:error/message "must be a 9-digits string"}
           #"^\d{9}$"]]])

(defn malli-validate []
  (->> (input)
       ; (take 10)
       ; (map (comp me/humanize (partial m/explain schema))))
       (filter #(m/validate schema %))
       count))

(defn malli-explain []
  (->> (input)
       (take 10)
       (map (comp
              ; identity
              me/humanize
              (partial m/explain schema)))))
