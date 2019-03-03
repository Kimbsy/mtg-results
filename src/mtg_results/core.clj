(ns mtg-results.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn get-all-tournaments
  []
  (let [dir (-> "results"
                io/resource
                io/file
                file-seq)
        files (filter #(.isFile %) dir)]
    (->> files
         (map slurp)
         (map edn/read-string))))

(defn get-all-decks
  []
  (map :deck (get-all-tournaments)))

(defn get-tournaments-by-deck-url
  [url]
  (filter (fn [{:keys [deck]}]
            (= url (:url deck)))
          (get-all-tournaments)))

(defn match-wld-rate-for-day
  [totals day]
  (reduce (fn [totals {:keys [result] :as m}]
            (update totals result inc))
          totals
          (:matches day)))

(defn match-wld-rate-for-tournament
  [totals tournament]
  (reduce match-wld-rate-for-day
          totals
          (:results tournament)))

(defn match-wld-rate
  [tournaments]
  (reduce match-wld-rate-for-tournament
          {:win 0
           :loss 0
           :draw 0}
          tournaments))

(match-wld-rate (get-all-tournaments))
(match-wld-rate (get-tournaments-by-deck-url "http://tappedout.net/mtg-decks/grn-ub-control-1/"))







(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
