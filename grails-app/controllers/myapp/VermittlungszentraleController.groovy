package myapp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class VermittlungszentraleController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Vermittlungszentrale.list(params), model:[vermittlungszentraleCount: Vermittlungszentrale.count()]
    }

    def show(Vermittlungszentrale vermittlungszentrale) {
        respond vermittlungszentrale
    }

    def create() {
        respond new Vermittlungszentrale(params)
    }

    @Transactional
    def save(Vermittlungszentrale vermittlungszentrale) {
        if (vermittlungszentrale == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (vermittlungszentrale.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond vermittlungszentrale.errors, view:'create'
            return
        }

        vermittlungszentrale.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'vermittlungszentrale.label', default: 'Vermittlungszentrale'), vermittlungszentrale.id])
                redirect vermittlungszentrale
            }
            '*' { respond vermittlungszentrale, [status: CREATED] }
        }
    }

    def edit(Vermittlungszentrale vermittlungszentrale) {
        respond vermittlungszentrale
    }

    @Transactional
    def update(Vermittlungszentrale vermittlungszentrale) {
        if (vermittlungszentrale == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (vermittlungszentrale.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond vermittlungszentrale.errors, view:'edit'
            return
        }

        vermittlungszentrale.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'vermittlungszentrale.label', default: 'Vermittlungszentrale'), vermittlungszentrale.id])
                redirect vermittlungszentrale
            }
            '*'{ respond vermittlungszentrale, [status: OK] }
        }
    }

    @Transactional
    def delete(Vermittlungszentrale vermittlungszentrale) {

        if (vermittlungszentrale == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        vermittlungszentrale.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'vermittlungszentrale.label', default: 'Vermittlungszentrale'), vermittlungszentrale.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'vermittlungszentrale.label', default: 'Vermittlungszentrale'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
