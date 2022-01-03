import React, {Component} from 'react';
import {Route, Navigate, Routes} from 'react-router-dom'
import Home from "./Home/Home";
import WorldMap from "./WorldMap/WorldMap";

class Display extends Component {
    render() {
        return (
            <div>
                <Routes>
                    <Route path="/home" element={<Home/>}/>
                    <Route path="/map" element={<WorldMap/>}/>
                    <Route path="*" element={<Navigate replace to="/home" />} />
                </Routes>
            </div>
        );
    }
}

export default Display;