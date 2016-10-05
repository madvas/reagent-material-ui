(ns reagent-material-ui.core
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :refer [get-mui-theme]]
            [cljs-react-material-ui.reagent :as ui]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]
            [reagent.impl.template]
            [reagent.interop :refer-macros [$ $!]]
            ))

;; -------------------------
;; Views

(set! reagent.impl.template/input-component?
      (fn [x]
        (or (= x "input")
            (= x "textarea")
            (= (when-let [prop-types ($ x :propTypes)]
                 (aget prop-types "inputStyle"))))))

(defn home-page []
  (let [text-state (atom "abc")]
    #_(js/setInterval #(reset! text-state (rand-int 9999)) 1000)
    (fn []
      [ui/mui-theme-provider {:mui-theme (get-mui-theme)}
       [:div [:h2 "Welcome to reagent-material-ui"]
        [:div
         [:a {:href "/about"} "go to about page"] [:br]
         [ui/text-field
          {:id "my-text-field"
           :value @text-state
           :on-change (fn [e] (reset! text-state (.. e -target -value)))}]
         [:br]
         [:input
          {:id "my-text-field"
           :value @text-state
           :on-change (fn [e] (reset! text-state (.. e -target -value)))}]]]])))

(defn about-page []
  [:div [:h2 "About reagent-material-ui"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
