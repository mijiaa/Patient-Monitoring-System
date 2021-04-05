import requests
import pandas as pd
from datetime import datetime
import csv
root_url = 'https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/'

dReport_url= root_url +"DiagnosticReport"
data3 = pd.DataFrame(columns =['patientid','gender', 'birthDate',  'maritualStatus', 'totalCholesterol',"Triglycerides"
    , 'lowDensity', 'highDensity', 'issued'] )

def checkDate(patient_id,new_date):
    # check whether the observation's issued date is the latest
    if patient_id not in data3.index:
        return True
    else:
        old_date = data3.loc[patient_id,'issued']
        if new_date > old_date:
            data3.drop([patient_id])
            return True
        else:
            return False


next_page = True
next_url = dReport_url
count_page = 0
count_patient = 0
count = 0
f = open('temp.csv', 'w+',newline='')
writer = csv.writer(f)
writer.writerow(["gender","age","cholesterol","dbp","sbp"])
while next_page == True and count<=20000:

    dReports = requests.get(url=next_url).json()

    # As discussed before, The monash FHIR server return results in a series of pages.
    # Each page contains 10 results as default.
    # here we check and record the next page
    next_page = False
    links = dReports['link']
    for i in range(len(links)):
        link = links[i]
        if link['relation'] == 'next':
            next_page = True
            next_url = link['url']
            count_page += 1

    # Extract data
    try:
        entry = dReports['entry']
        for i in range(len(entry)):
            patient_array = []
            results = entry[i]['resource']['result']

            # Check whether this observation is on chterol or not.
            chterol = False
            sbp = True
            for result in results:
                if result['display'] == 'Total Cholesterol':
                    chterol = True
                    observation_ref = result['reference']
                    # print(observation_ref)


            # If this observation is on cholesterol value, then record the patient's id and issued date.
            if chterol == True:
                patient_id = entry[i]['resource']['subject']['reference'][len('Patient/'):]
                encounter_id = entry[i]['resource']['encounter']['reference'][len('Encounter/'):]
                issued = entry[i]['resource']['issued'][:len('2008-10-14')]
                date = datetime.strptime(issued, '%Y-%m-%d').date()


                # Get patient's basic information
                patient_data = requests.get(url=root_url + "Patient/" + patient_id).json()
                gender = patient_data['gender']
                birth = patient_data['birthDate']
                birthDate = int(birth[:4])
                age =  2020 - birthDate


                check = checkDate(patient_id,date)

                # Check if the patient's Chterol value has already been recorded in the dataframe
                if check == True:
                    count_patient += 1
                    # patient_array.append(patient_id)
                    # patient_array.append(gender)
                    # patient_array.append(age)

                    # Record chtoral(including total, Triglycerides, lowDensity and highDensity) value

                    observation_data = requests.get(url=root_url + observation_ref).json()
                    try:
                        bp_data = requests.get(
                            url=root_url + "Observation?encounter=Encounter/" + encounter_id + "&code=55284-4").json()
                        temp = bp_data["entry"]
                        for res in temp:
                            resource= res["resource"]
                            dbp = resource['component'][0]['valueQuantity']['value']
                            sbp = resource['component'][1]['valueQuantity']['value']
                        value = observation_data['valueQuantity']['value']
                        if gender == 'male':
                            gender = 0
                        else:
                            gender = 1
                        writer.writerow([gender,age,value,dbp,sbp])
                        # f.write("\n")
                        count+=1
                        print(count)
                    except:
                        continue
    except:
        continue
                # patient_array.append(value)
                # patient_array.append(dbp)
                # patient_array.append(sbp)
                # patient_array.append(date)
                # print(patient_array)
                # data3.append(patient_array)
                # patientId.append(patient_id)

