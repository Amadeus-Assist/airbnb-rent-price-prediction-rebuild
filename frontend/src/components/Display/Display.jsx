import React from 'react';
import {Route, Navigate, Routes} from 'react-router-dom'
import Home from "../../pages/Home/Home";
import WorldMap from "../../pages/WorldMap/WorldMap";
import './Dispaly.css'
import CityView from "../../pages/CityView/CityView";

function Display(){
    return (
        <div className="outer-container">
            <Routes>
                <Route path="/home" element={<Home/>}/>
                <Route path="/map" element={<WorldMap/>}/>
                <Route path="/cityview/:city/*" element={<CityView/>}/>
                <Route path="*" element={<Navigate replace to="/home"/>}/>
            </Routes>
        </div>
    );
}

export default Display;