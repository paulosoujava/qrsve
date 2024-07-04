package com.soujava.mydoctor.core

fun main() {

    val jsonTriage = parseJson(json)
    println(jsonTriage)

}



val json ="""
    {"items": [{"type": "ONE_LINE_TEXT_FIELD", "typeField": "Númerico", "label": "Quantidade de vezes que você colicava por dia?"}, {"type": "CHECKBOX", "label": "Você sentia dor ao colicar?", "options": ["Sim", "Não"]}, {"type": "MORE_LINE_TEXT_FIELD", "label": "Descreva como era a sua cólica"}, {"type": "RADIO_GROUP", "label": "Com qual frequência você colicava?", "options": ["Diariamente", "Semanalmente", "Mensalmente", "Raramente"]}, {"type": "SLIDER", "label": "Em uma escala de 0 a 10, sendo 0 nenhuma dor e 10 a pior dor possível, como você classificaria a intensidade da sua cólica?", "min": 0, "max": 10}], "hiddenToDoctor": ""}
"""