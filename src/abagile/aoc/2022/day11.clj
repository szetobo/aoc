(ns abagile.aoc.2022.day11
  (:require
   [clojure.string :as cs]))

(def input
  (->> (map cs/split-lines (-> "resources/2022/day11.txt" slurp (cs/split #"\n\n")))
    (map (fn [[_ items op check true-op false-op]]
           {:items     (mapv read-string (re-seq #"\d+" items))
            :op        (->> (re-find #"(\+|\*) (\d+|old)" op)
                            rest)
            :divide-by (-> (re-find #"divisible by (\d+)" check) second read-string)
            :throw-to  (map read-string [(second (re-find #"throw to monkey (\d+)" true-op))
                                         (second (re-find #"throw to monkey (\d+)" false-op))])}))))

(defn prepare1
  [input]
  (mapv (fn [{[op param] :op
              [mk1 mk2]  :throw-to :keys [divide-by] :as monkey}]
          (assoc monkey
                 :item-fn #(quot ((case op "*" * "+" +) % (if (= param "old") % (read-string param))) 3)
                 :op-fn   #(if (zero? (rem % divide-by)) mk1 mk2)))
        input))

(defn turn
  [monkeys n]
  (let [{:keys [items item-fn op-fn]} (get monkeys n)]
    (-> (reduce #(let [item (item-fn %2)]
                   (update-in %1 [(op-fn item) :items] conj item))
                monkeys items)
        (update-in [n :inspected] (fnil conj []) (count items))
        (assoc-in [n :items] []))))

(defn part1
  []
  (->> (iterate #(reduce turn % (range (count input))) (prepare1 input))
       (drop 20) first
       (map :inspected)
       (map #(reduce + %)) (sort >) (take 2) (reduce *)))

(defn prepare2
  [input]
  (let [magic-num (->> (map :divide-by input) (reduce *))]
    (mapv (fn [{[op param] :op
                [mk1 mk2]  :throw-to :keys [divide-by] :as monkey}]
            (assoc monkey
                   :item-fn #(mod ((case op "*" *' "+" +') % (if (= param "old") % (read-string param))) magic-num)
                   :op-fn   #(if (zero? (rem % divide-by)) mk1 mk2)))
          input)))

(defn part2
  []
  (->> (iterate #(reduce turn % (range (count input))) (prepare2 input))
       (drop 10000) first
       (map :inspected)
       (map #(reduce + %)) (sort >) (take 2) (reduce *)))

(defn -main [& _]
  (println "part 1:" (time (part1)))
  (println "part 2:" (time (part2))))

(comment
  (-main))
