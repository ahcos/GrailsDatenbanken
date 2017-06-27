package myapp

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BundeslandController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Bundesland.list(params), model:[bundeslandCount: Bundesland.count()]
    }

    def show(Bundesland bundesland) {
        respond bundesland
    }

    def create() {
        respond new Bundesland(params)
    }

    @Transactional
    def save(Bundesland bundesland) {
        if (bundesland == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bundesland.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bundesland.errors, view:'create'
            return
        }

        bundesland.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bundesland.label', default: 'Bundesland'), bundesland.id])
                redirect bundesland
            }
            '*' { respond bundesland, [status: CREATED] }
        }
    }

    def edit(Bundesland bundesland) {
        respond bundesland
    }

    @Transactional
    def update(Bundesland bundesland) {
        if (bundesland == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bundesland.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bundesland.errors, view:'edit'
            return
        }

        bundesland.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'bundesland.label', default: 'Bundesland'), bundesland.id])
                redirect bundesland
            }
            '*'{ respond bundesland, [status: OK] }
        }
    }

    @Transactional
    def delete(Bundesland bundesland) {

        if (bundesland == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        bundesland.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'bundesland.label', default: 'Bundesland'), bundesland.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bundesland.label', default: 'Bundesland'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
