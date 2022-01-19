(ns abagile.aoc.2015.day21
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.math.combinatorics :as comb]
    [clojure.test :refer [deftest is]]))

(def input  (->> (util/read-input "2015/day21.txt")))

(defn parse
  [data]
  (let [[hp damage armor] (->> data (re-seq #"\d+") (map read-string))]
    {:boss    {:id :boss   :hp hp  :damage damage :armor armor}
     :player  {:id :player :hp 100 :damage 0      :armor 0}
     :weapons {:dagger     {:cost 8   :damage 4 :armor 0}
               :shortsword {:cost 10  :damage 5 :armor 0}
               :warhammer  {:cost 25  :damage 6 :armor 0}
               :longsword  {:cost 40  :damage 7 :armor 0}
               :greataxe   {:cost 74  :damage 8 :armor 0}}
     :armors  {:leather    {:cost 13  :damage 0 :armor 1}
               :chainmail  {:cost 31  :damage 0 :armor 2}
               :splintmail {:cost 53  :damage 0 :armor 3}
               :bandedmail {:cost 75  :damage 0 :armor 4}
               :platemail  {:cost 102 :damage 0 :armor 5}}
     :rings   {:damage+1   {:cost 25  :damage 1 :armor 0}
               :damage+2   {:cost 50  :damage 2 :armor 0}
               :damage+3   {:cost 100 :damage 3 :armor 0}
               :defense+1  {:cost 20  :damage 0 :armor 1}
               :defense+2  {:cost 40  :damage 0 :armor 2}
               :defense+3  {:cost 80  :damage 0 :armor 3}}}))

(defn play
  [defender attacker]
  (if (-> attacker :hp pos?)
    (recur attacker (update defender :hp - (max 1 (- (:damage attacker) (:armor defender)))))
    defender))

(defn part1 []
  (time
    (let [{:keys [weapons armors rings player boss]} (parse input)]
      (->> (for [wn (keys weapons)
                 an (cons nil (keys armors))
                 [rn1 rn2] (mapcat #(comb/combinations (keys rings) %) [0 1 2])
                 :let [stats (merge-with + (weapons wn) (armors an) (rings rn1) (rings rn2))]
                 :when (= :player (:id (play boss (merge-with + player stats))))]
             (assoc stats :equipments [wn an rn1 rn2]))
           (map :cost)
           (apply min)))))

(defn part2 []
  (time
    (let [{:keys [weapons armors rings player boss]} (parse input)]
      (->> (for [wn (keys weapons)
                 an (cons nil (keys armors))
                 [rn1 rn2] (mapcat #(comb/combinations (keys rings) %) [0 1 2])
                 :let [stats (merge-with + (weapons wn) (armors an) (rings rn1) (rings rn2))]
                 :when (= :boss (:id (play boss (merge-with + player stats))))]
             (assoc stats :equipments [wn an rn1 rn2]))
           (map :cost)
           (apply max)))))
    

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest test-sample
  (is (= (play {:hp 12 :damage 7 :armor 2} {:hp 8  :damage 5 :armor 5})
         {:hp 2 :damage 5 :armor 5})))
