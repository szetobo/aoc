(ns abagile.aoc.2015.day07
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest is]]))

(def sample (util/read-input-split "2015/day07.sample.txt" #"\n"))
(def input  (util/read-input-split "2015/day07.txt" #"\n"))

(def signal #(cond-> % (re-matches #"^\d+$" %) read-string))

(def unsigned-bit-not #(-> % bit-not (bit-and 16rFFFF)))

(defn parse
  [wire]
  (condp re-matches wire
    #"^(\w+)\s*->\s*(\w+)$"                  :>> (fn [[_ p g]]     {:gate g :fn identity         :signals [(signal p)]})
    #"^NOT\s+(\w+)\s*->\s*(\w+)$"            :>> (fn [[_ p g]]     {:gate g :fn unsigned-bit-not :signals [(signal p)]})
    #"^(\w+)\s+AND\s+(\w+)\s*->\s*(\w+)$"    :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-and          :signals [(signal p1) (signal p2)]})
    #"^(\w+)\s+OR\s+(\w+)\s*->\s*(\w+)$"     :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-or           :signals [(signal p1) (signal p2)]})
    #"^(\w+)\s+LSHIFT\s+(\d+)\s*->\s*(\w+)$" :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-shift-left   :signals [(signal p1) (signal p2)]})
    #"^(\w+)\s+RSHIFT\s+(\d+)\s*->\s*(\w+)$" :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-shift-right  :signals [(signal p1) (signal p2)]})))

(comment
  (map parse sample)
  (map parse input))

(defn eval-gate
  [env {:keys [gate fn signals]}]
  (let [signal-vals (map #(get env % %) signals)]
    (cond-> env (every? number? signal-vals) (assoc gate (apply fn signal-vals)))))

(defn eval-gates
  [circuit]
  (loop [env {} wires circuit]
    (if (empty? wires)
      env
      (recur (reduce eval-gate env wires) (remove #(env (:gate %)) wires)))))

(defn part1
  []
  (time
    (-> (map parse input) eval-gates (get "a"))))
    

(defn part2
  []
  (time
    (let [env (->> (map parse input)
                   (map #(if (= "b" (:gate %)) {:gate "b" :fn identity :signals [3176]} %))
                   eval-gates)]
      (get env "a"))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= {:gate "x" :fn identity         :signals [123]}     (parse "123 -> x")))
  (is (= {:gate "y" :fn identity         :signals [456]}     (parse "456 -> y")))
  (is (= {:gate "d" :fn bit-and          :signals ["x" "y"]} (parse "x AND y -> d")))
  (is (= {:gate "e" :fn bit-or           :signals ["x" "y"]} (parse "x OR y -> e")))
  (is (= {:gate "f" :fn bit-shift-left   :signals ["x" 2]}   (parse "x LSHIFT 2 -> f")))
  (is (= {:gate "g" :fn bit-shift-right  :signals ["y" 2]}   (parse "y RSHIFT 2 -> g")))
  (is (= {:gate "h" :fn unsigned-bit-not :signals ["x"]}     (parse "NOT x -> h")))
  (is (= {:gate "i" :fn unsigned-bit-not :signals ["y"]}     (parse "NOT y -> i")))
  (is (= {"x" 123 "y" 456 "d" 72 "e" 507 "f" 492 "g" 114 "h" 65412 "i" 65079} (eval-gates (map parse sample)))))
