(ns reagent-material-ui.prod
  (:require [reagent-material-ui.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
