(ns abagile.aoc.2020.day21
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as set]
    [clojure.string :as cs]
    [clojure.test :refer [deftest run-tests is]]
    [instaparse.core :as insta]))

(def input (->> (util/read-input-split-lines "2020/day21.txt")))

(def sample ["mxmxvkd kfcds sqjhc nhms (contains dairy, fish)"
             "trh fvjkl sbzzf mxmxvkd (contains dairy)"
             "sqjhc fvjkl (contains soy)"
             "sqjhc mxmxvkd sbzzf (contains fish)"])

(def rule-parser (insta/parser "S = IS <'(contains '> AS <')'>
                                IS = I+
                                AS = A | A (<','> A)+
                                <space> = <#'\\s*'>
                                I = (space #'\\w+' space)
                                A = (space #'\\w+' space)"))

(def transform-opts {:I identity
                     :IS (comp set vector)
                     :A identity
                     :AS vector
                     :S vector})

(defn parse [data]
  (->> (map rule-parser data)
       (map (partial insta/transform transform-opts))))

(defn allergens [data]
  (->> (mapcat (fn [[i as]] (map #(hash-map % i) as)) data)
       (apply merge-with set/intersection)
       (#(loop [last-found []
                data %]
           (let [{:keys [found remaining]} (group-by (fn [[_k v]] (if (= (count v) 1) :found :remaining)) data)]
             (if (empty? found)
               last-found
               (recur (concat last-found found)
                      (map (fn [[a i]]
                             [a (set/difference i (reduce set/union (map second found)))])
                           remaining))))))))

(defn part1 []
  (time (let [data (parse input)]
         (->> (mapcat first data)
              (remove (apply set/union (map second (allergens data))))
              count))))

(defn part2 []
  (time (let [data (parse input)]
          (->> (allergens data)
               (sort-by first)
               (map (fn [[_ ing]] (first ing)))
               (cs/join ",")))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (let [data (parse sample)]
    (is (= data [[#{"mxmxvkd" "kfcds" "sqjhc" "nhms"} ["dairy" "fish"]]
                 [#{"trh" "fvjkl" "sbzzf" "mxmxvkd"} ["dairy"]]
                 [#{"sqjhc" "fvjkl"} ["soy"]]
                 [#{"sqjhc" "mxmxvkd" "sbzzf"} ["fish"]]]))
    (is (= (allergens data) [["dairy" #{"mxmxvkd"}] ["fish" #{"sqjhc"}] ["soy" #{"fvjkl"}]]))))

(deftest test-input
  (let [data (parse input)]
    (is (= (allergens data) [["sesame" #{"kfgln"}] ["eggs" #{"jmvxx"}] ["shellfish" #{"pqqks"}]
                             ["fish" #{"lkv"}] ["peanuts" #{"cbzcgvc"}] ["dairy" #{"fdsfpg"}]
                             ["soy" #{"pqrvc"}] ["wheat" #{"lclnj"}]]))))

(run-tests)
