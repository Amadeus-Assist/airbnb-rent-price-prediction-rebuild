import React from 'react';
import {Link} from "react-router-dom";

function Menu(){
    return (
        <div className="ui container">
            <div className="ui secondary menu">
                <Link className="item" to="/home">Home</Link>
                <Link className="item" to="/map">World Map</Link>
            </div>
        </div>
    );
}

export default Menu;