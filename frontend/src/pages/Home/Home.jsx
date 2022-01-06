import React, {Component} from 'react';
import {withRouter} from '../../withRouter'

class Home extends Component {
    state = {cityInput: ''}


    saveValue = (event) => {
        this.setState({cityInput: event.target.value})
    }

    doSearch = async () => {
        const {cityInput} = this.state;
        if (!(typeof cityInput == "undefined" || cityInput == null || cityInput === '')) {
            console.log("cityInput: ", cityInput)
            const response = await fetch(`/api/getSimilarCity/${cityInput}`);
            // const response = await fetch(`/api/hello`);
            const data = await response.json();
            const {city} = data;
            this.props.navigate(`/cityview/${city}`)
        }
    }

    render() {
        return (
            <div className="ui vertical masthead center aligned segment">
                <div className="ui text container">
                    <h1 className="ui header">Airbnb Rent Price Prediction</h1>
                    <h2>Input city names to navigate!</h2>
                    <br/>
                    <div className="ui fluid action input">
                        <input onChange={this.saveValue} type="text" id="cityName" placeholder="Search..."/>
                        <button onClick={this.doSearch} className="ui primary button" id="viewCity">View Maps</button>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(Home);