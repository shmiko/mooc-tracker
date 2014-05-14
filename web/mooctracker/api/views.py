from django.http import HttpResponse
import json

from students.models import Student

STATUS_OK = {
  'success' : 'API is running'
}

# status view
def status(request):  

  return HttpResponse(
    json.dumps(STATUS_OK),
    content_type = 'application/json'
  )

# students api implementing GET, POST, PUT & DELETE for Student model
def students(request,id=0):

  if request.method == 'GET':

    response = []
    students = Student.objects.all()
    for student in students:
      print student
      obj = {}
      obj.update({ 'id': student.id })
      obj.update({ 'name': student.name })
      response.append(obj)

    return HttpResponse(
    json.dumps(response),
    content_type = 'application/json')

  elif request.method == 'POST':
    studentName = request.POST['name']
    newStudent = Student(name = studentName)
    newStudent.save()
    addedStudent = {'id' : newStudent.id , 'name' : newStudent.name}

    
    return HttpResponse(
      json.dumps(addedStudent),
      content_type='application/json')

  elif request.method == 'DELETE':
    Student.objects.get(id=id).delete()
    message={ 'success' : 'True' }
    return HttpResponse(
      json.dumps(message),
      content_type='application/json')

    