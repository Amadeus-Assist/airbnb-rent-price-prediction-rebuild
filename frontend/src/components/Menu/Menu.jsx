import React, {Component} from 'react';
import {Link} from "react-router-dom";

class Menu extends Component {
    render() {
        return (
            <div className="ui container">
                <div className="ui secondary menu">
                    <Link className="item" to="/home">Home</Link>
                    <Link className="item" to="/map">World Map</Link>
                </div>
            </div>
        );
    }
}

export default Menu;