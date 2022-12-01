# medical-clinic-management-system

CLINIC MANAGEMENT SYSTEM
• The clinic employs doctors of various specialties (a doctor may have several specialties).
        Patient and doctor - has contact details,
        Patient has a date of birth and gender,
        Doctor specializations and hourly rate.
• Doctors can see patients from Monday to Friday from 8:00 to 18:00.
• Patients make appointments with a given specialist for 15-minute visits. Each visit has the following status: COMPLETED, PENDING, NOT REALIZED_PATIENT (due to the patient's fault), NOT REALIZED_OTHER (for any other reason).
• The system stores full details of visits.
• The system allows patients to make an appointment with a general specialist or with a specific doctor of a given specialization.
• You cannot make an appointment with an unsuitable specialist based on age and gender. The system allows you to add a doctor's duty in the clinic. A doctor may not perform more than one duty at a given period. Duty has a date from to and an assigned doctor
• Patients who have not visited a given specialist twice in the last month cannot make another appointment for a month from the last unrealized visit (does not apply to a pediatrician/internist).

DATABASE QUERY:
1. All visits according to the given criteria (criteria are optional):
• Visit status,
• Date and time range,
• Name of the specialist
• Patient's first or last name
2. Total cost of patient visits in a given period
3. Patient who missed the most visits in a given time
4. Physicians with the fewest appointments in a given period
5. 2 most visited doctors
6. Doctors of a given specialization who can see a patient on a specific day
7. Name the specialization for which visits were most often not carried out
