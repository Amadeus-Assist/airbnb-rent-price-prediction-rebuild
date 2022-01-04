import React, {Component} from 'react';

class Home extends Component {
    render() {
        return (
            <div className="ui vertical masthead center aligned segment">
                <div className="ui text container">
                    <h1 className="ui header">Airbnb Rent Price Prediction</h1>
                    <h2>Input city names to navigate!</h2>
                    <br/>
                    <div className="ui fluid action input">
                        <input type="text" id="cityName" placeholder="Search..."/>
                        <button className="ui primary button" id="viewCity">View Maps</button>
                    </div>
                </div>
            </div>
        );
    }
}

export default Home;