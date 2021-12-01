(ns abagile.aoc.pair.2015-21
  (:gen-class)
  (:require
    [clojure.test :refer [deftest is]]))

; Player            vs  Boss
; ======                ====
; Hit Points: 100       Hit Points: 109
; Damage: 0             Damage: 8
; Armor: 0              Armor 2


(def farmer' {:hp 8 :atk 5 :def 5})
(def boss' {:hp 12 :atk 7 :def 2})

(def farmer {:hp 100 :atk 0 :def 0 :cost 0})
(def boss {:hp 109 :atk 8 :def 2})

(def weapons {:dagger     {:cost 8   :atk 4 :def 0}
              :shortsword {:cost 10  :atk 5 :def 0}
              :warhammer  {:cost 25  :atk 6 :def 0}
              :longsword  {:cost 40  :atk 7 :def 0}
              :greataxe   {:cost 74  :atk 8 :def 0}})

(def armors {:leather    {:cost 13  :atk 0 :def 1}
             :chainmail  {:cost 31  :atk 0 :def 2}
             :splintmail {:cost 53  :atk 0 :def 3}
             :bandedmail {:cost 75  :atk 0 :def 4}
             :platemail  {:cost 102 :atk 0 :def 5}
             :none       {:cost 0   :atk 0 :def 0}})

(def rings {:atk+1   {:cost 25  :atk 1 :def 0}
            :atk+2   {:cost 50  :atk 2 :def 0}
            :atk+3   {:cost 100 :atk 3 :def 0}
            :defense+1  {:cost 20  :atk 0 :def 1}
            :defense+2  {:cost 40  :atk 0 :def 2}
            :defense+3  {:cost 80  :atk 0 :def 3}
            :none1     {:cost 0   :atk 0 :def 0}
            :none2     {:cost 0   :atk 0 :def 0}})

(defn soldier [player equips]
  (-> player
      (update :atk (fn [atk] (+ atk (reduce + (map :atk equips)))))
      (update :def (fn [def] (+ def (reduce + (map :def equips)))))
      (update :cost (fn [cost] (+ cost (reduce + (map :cost equips)))))))

(defn fight [player boss]
  (let [atk-caused (max  1 (- (:atk player) (:def boss)))]
    (Math/ceil (/ (:hp boss) atk-caused))))

(defn win [player boss]
  (<= (fight player boss) (fight boss player)))

(defn equips-selections []
  (for [weapon weapons
        armor armors
        ring1 rings
        ring2 rings
        :when (not= ring1 ring2)
        :let [fighter (soldier farmer [(second weapon) (second armor) (second ring1) (second ring2)])]
        :when (win fighter boss)]
    [(:cost fighter) fighter weapon armor ring1 ring2]))

(apply min (map first (equips-selections)))
(filter #(= 111 (first %)) (equips-selections))
(filter #(= 8 (first %)) (equips-selections))

(def input nil)

(defn part1 [])

(defn part2 [])

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-input
  (is (= (fight farmer' boss') 4.0))
  (is (= (win farmer' boss') true))
  (is (= (soldier farmer [{:cost 8 :atk 4 :def 0}
                          {:cost 50  :atk 2 :def 1}]) {:hp 100 :atk 6 :def 1 :cost 58}))
  (is (= (soldier farmer [{:cost 8 :atk 4 :def 0}
                          {:cost 50  :atk 99 :def 99}]) {:hp 100 :atk 103 :def 99 :cost 58}))
  (is (= (fight {:hp 100 :atk 103 :def 99 :cost 58} boss) 2.0))
  (is (= (fight boss {:hp 100 :atk 103 :def 99 :cost 58}) 100.0))
  (is (= (win {:hp 100 :atk 103 :def 99 :cost 58} boss) true))
  (is (= (win
           (soldier farmer [{:cost 8 :atk 4 :def 0} {:cost 50  :atk 99 :def 99}])
           boss)
         true)))
