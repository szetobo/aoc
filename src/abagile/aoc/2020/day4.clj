(ns abagile.aoc.2020.day4
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.spec.alpha :as s]
    [malli.core :as m]
    [malli.error :as me]))

(def input (->> (util/read-input-split "2020/day4.txt" #"\n\n")
                (map #(re-seq #"(byr|iyr|eyr|hgt|hcl|ecl|pid|cid):([^\s]+)" %))
                (map #(into (hash-map) (map (comp vec rest) %)))))

(defn valid? [part]
  (fn [m]
    (let [{:strs [byr iyr eyr hgt hcl ecl pid]} m]
      (and
        (every? some? [byr iyr eyr hgt hcl ecl pid])
        (if (= part 1) true
          (and
            (let [byr (re-matches #"\d+" byr)] (<= 1920 (util/parse-int byr) 2002))
            (let [iyr (re-matches #"\d+" iyr)] (<= 2010 (util/parse-int iyr) 2020))
            (let [eyr (re-matches #"\d+" eyr)] (<= 2020 (util/parse-int eyr) 2030))
            (let [[_ cm in] (re-matches #"(\d{3})cm|(\d{2})in" hgt)]
              (or (<= 150 (util/parse-int cm) 193)
                  (<= 59  (util/parse-int in) 76)))
            (re-matches #"#[0-9|a-f]{6}" hcl)
            (re-matches #"amb|blu|brn|gry|grn|hzl|oth" ecl)
            (re-matches #"\d{9}" pid)))))))

(defn part1 []
  (time (->> (count (filter (valid? 1) input)))))

(defn part2 []
  (time (->> (count (filter (valid? 2) input)))))

(s/def ::byr (s/and string? #(<= 1920 (util/parse-int %) 2002)))
(s/def ::iyr (s/and string? #(<= 2010 (util/parse-int %) 2020)))
(s/def ::eyr (s/and string? #(<= 2020 (util/parse-int %) 2030)))
(s/def ::hgt (s/and string? #(let [[_ cm in] (re-matches #"(\d{3})cm|(\d{2})in" %)]
                               (or (<= 150 (util/parse-int cm) 193)
                                   (<= 59  (util/parse-int in) 76)))))
(s/def ::hcl (s/and string? (partial re-matches #"#[0-9|a-f]{6}")))
(s/def ::ecl (s/and string? (partial re-matches #"amb|blu|brn|gry|grn|hzl|oth")))
(s/def ::pid (s/and string? (partial re-matches #"\d{9}")))
(s/def ::cid (s/and string?))

(s/def ::passport (s/keys :req-un [::byr ::iyr ::eyr ::hgt ::hcl ::ecl ::pid]
                          :opt-un [::cid]))

(defn spec-validate []
  (->> input
       (map (fn [m] (reduce-kv #(assoc %1 (keyword %2) %3) {} m)))
       (filter #(s/valid? ::passport %))
       count))

(def schema
  [:map
   ["byr" [:fn {:error/message "must be between 1920 and 2002"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 1920 (util/parse-int v) 2002))]]
   ["iyr" [:fn {:error/message "must be between 2010 and 2020"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 2010 (util/parse-int v) 2020))]]
   ["eyr" [:fn {:error/message "must be between 2020 and 2030"}
           #(let [v (re-matches #"^\d{4}$" %)] (<= 2020 (util/parse-int v) 2030))]]
   ["hgt" [:fn {:error/message "must be between 150cm and 193cm, or 59in and 76in"}
           #(let [[_ v unit] (re-matches #"(\d+)(cm|in)" %)]
              (if (= "cm" unit)
                (<= 150 (util/parse-int v) 193)
                (<= 59  (util/parse-int v) 76)))]]
   ["hcl" [:re {:error/message "must be a valid hex color string, e.g #c0c0c0"}
           #"^#[0-9|a-f]{6}$"]]
   ["ecl" [:enum {:error/message "must be amb|blu|brn|gry|grn|hzl|oth"}
           "amb" "blu" "brn" "gry" "grn" "hzl" "oth"]]
   ["pid" [:re {:error/message "must be a 9-digits string"}
           #"^\d{9}$"]]])

(defn malli-validate []
  (->> input
       ; (take 10)
       ; (map (comp me/humanize (partial m/explain schema))))
       (filter #(m/validate schema %))
       count))

(defn malli-explain []
  (->> input
       (take 10)
       (map (comp
              ; identity
              me/humanize
              (partial m/explain schema)))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
