(ns abagile.aoc.2023.day12
  (:require
   [abagile.aoc.util :as util]
   [clojure.math.combinatorics :as comb]
   [clojure.string :as cs]))

(def input (->> (util/read-input-split "2023/day12.txt" #"\n")))

(defn parse
  [input]
  (->> input
       (map #(-> %
                 (cs/split #" ")
                 (as-> $ (let [[form1 form2] $]
                           [form1 (map read-string (cs/split form2 #","))]))))))

(defn combine
  [form combs]
  (map
   (fn [comb] (->> comb seq (reduce #(cs/replace-first %1 \? %2) form)))
   combs))

(comment
  (combine "??.???.#??" [".#.#.#." "...#.#."]))

(defn solve
  [[form1 form2]]
  (let [re (-> (str "^\\.*"
                    (cs/join "\\.+" (map #(apply str (repeat % "#")) form2))
                    "\\.*$")
               (re-pattern))]
    (->> (for [s (->> (repeat (-> form1 (cs/escape {\. "" \# ""}) count) [\. \#])
                      (apply comb/cartesian-product)
                      (map #(apply str %))
                      (combine form1))
               :when (re-matches re s)]
           s)
         count)))
     
(defn part1
  []
  (time (->> input parse (map solve) (reduce +))))


(defn part2
  []
  (time (->> input)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))
