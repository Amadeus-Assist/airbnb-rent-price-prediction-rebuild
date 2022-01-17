import React, {useState, useEffect} from 'react';
import {useParams} from "react-router-dom";
import SimpleLineChart from "../../../components/Chart/SimpleLineChart/SimpleLineChart";
import './PredictionCharts.css'

function PredictionCharts() {
    const params = useParams()
    const {city} = params
    const [covidPrediction, setCovidPrediction] = useState()
    const [covidReady, setCovidReady] = useState(false)
    const [housingPrediction, setHousingPrediction] = useState()
    const [housingReady, setHousingReady] = useState(false)

    const covidPreConfig = {
        caption: "Predicted Daily New Cases COVID-19",
        adjustDiv: "0",
        yAxisMinValue: "0",
        labelFontSize: "10",
        drawAnchors: "0",
        theme: "candy",
        labelDisplay: "rotate",
        slantLabel: "1"
    }

    const housingPreConfig = {
        caption: "Predicted Housing Median Price",
        adjustDiv: "0",
        yAxisMinValue: "0",
        labelFontSize: "10",
        drawAnchors: "0",
        showvalues: "0",
        theme: "fusion",
        labelDisplay: "rotate",
        slantLabel: "1",
    }

    useEffect(()=>{
        async function fetchCovidPredictionData() {
            const response = await fetch(`/api/prediction/covid/${city}`)
            const predictionData = await response.json()

            const {covidPrediction} = predictionData
            setCovidPrediction(covidPrediction)
            setCovidReady(true)
            // console.log("Returned Date: ", covidHistoryDate)
            // console.log("Returned Data: ", covidHistoryData)
        }

        async function fetchHousingPredictionData() {
            const response = await fetch(`/api/prediction/housing/${city}`)
            const predictionData = await response.json()

            const {housingPrediction} = predictionData
            setHousingPrediction(housingPrediction)
            setHousingReady(true)
            // console.log("Returned Date: ", covidHistoryDate)
            // console.log("Returned Data: ", covidHistoryData)
        }

        fetchCovidPredictionData().catch(console.err)
        fetchHousingPredictionData().catch(console.err)
    }, [city])


    return (
        <div className="predictioncharts">
            <div className="covidPredictionChart">
                {covidReady ? <SimpleLineChart data={covidPrediction} config={covidPreConfig} id="covidPrediction"/> :
                    <div/>}
            </div>
            <div className="housingPredictionChart">
                {housingReady ?
                    <SimpleLineChart data={housingPrediction} config={housingPreConfig} id="housingPrediction"/> :
                    <div/>}
            </div>
        </div>
    );
}

export default PredictionCharts;